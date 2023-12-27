package com.innowise.authmicroservice.service.impl;

import avro.ClientActionRequest;
import avro.DeleteClientRequest;
import com.innowise.authmicroservice.dto.ClientDto;
import com.innowise.authmicroservice.dto.UpdateClientDto;
import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.enums.ActionEnum;
import com.innowise.authmicroservice.enums.ActionTypeEnum;
import com.innowise.authmicroservice.exception.BadRequestException;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.kafka.KafkaListeners;
import com.innowise.authmicroservice.kafka.KafkaProducer;
import com.innowise.authmicroservice.mapper.ClientMapperImpl;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.security.service.CustomUserDetails;
import com.innowise.authmicroservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final KafkaProducer kafkaProducer;
    private final KafkaListeners kafkaListeners;

    @Override
    public List<ClientDto> getClients(Integer offset, Integer limit) {
        kafkaProducer.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_CLIENTS.getAction(),
                ActionTypeEnum.GET_CLIENTS_TYPE.getActionType(),
                LocalDateTime.now().toString()
        ));
        return ClientMapperImpl.INSTANCE.toClientDtoList(clientRepository.findAll(PageRequest.of(offset, limit)).getContent());
    }

    @Override
    public ClientDto getClientById(Long clientId) throws NotFoundException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        kafkaProducer.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_CLIENT.getAction(),
                ActionTypeEnum.GET_CLIENT_TYPE.getActionType(),
                LocalDateTime.now().toString()
        ));
        return ClientMapperImpl.INSTANCE.clientEntityToClientDto(optionalClient.get());
    }

    @Override
    public ClientDto updateClientById(Long clientId, UpdateClientDto updateClientDto) throws NotFoundException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        ClientEntity client = optionalClient.get();
        kafkaProducer.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.UPDATE_CLIENT.getAction(),
                ActionTypeEnum.UPDATE_CLIENT_TYPE.getActionType(),
                LocalDateTime.now().toString()
        ));
        return ClientMapperImpl.INSTANCE.clientEntityToClientDto(clientRepository.save(ClientMapperImpl.INSTANCE.updateClientDtoToClientEntity(updateClientDto, client)));
    }

    @Override
    public String deleteClientById(Long clientId) throws NotFoundException, BadRequestException, InterruptedException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        kafkaProducer.sendDeleteClientRequest(new DeleteClientRequest(clientId));
        boolean isDeleted = kafkaListeners.waitForDeleteClientResponse().getIsDeleted();
        if (isDeleted) {
            clientRepository.delete(optionalClient.get());
            kafkaProducer.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.DELETE_CLIENT.getAction(),
                    ActionTypeEnum.DELETE_CLIENT_TYPE.getActionType(),
                    LocalDateTime.now().toString()
            ));
            return "Client with id = " + clientId + " was successfully deleted";
        } else {
            throw new BadRequestException("Can't delete client with id = " + clientId + " because they have rent or reservation");
        }
    }
}
