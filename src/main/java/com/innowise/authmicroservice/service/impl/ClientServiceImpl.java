package com.innowise.authmicroservice.service.impl;

import com.innowise.authmicroservice.dto.ClientDto;
import com.innowise.authmicroservice.dto.UpdateClientDto;
import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.exception.BadRequestException;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.mapper.ClientMapperImpl;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final RestTemplate restTemplate;
    @Value(value = "${carservice.url}")
    private String carEndpointUrl;

    @Override
    public List<ClientDto> getClients(Integer offset, Integer limit) {
        return ClientMapperImpl.INSTANCE.toClientDtoList(clientRepository.findAll(PageRequest.of(offset, limit)).getContent());
    }

    @Override
    public ClientDto getClientById(Long clientId) throws NotFoundException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if(optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        return ClientMapperImpl.INSTANCE.clientEntityToClientDto(optionalClient.get());
    }

    @Override
    public ClientDto updateClientById(Long clientId, UpdateClientDto updateClientDto) throws NotFoundException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if(optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        ClientEntity client = optionalClient.get();
        return ClientMapperImpl.INSTANCE.clientEntityToClientDto(clientRepository.save(ClientMapperImpl.INSTANCE.updateClientDtoToClientEntity(updateClientDto, client)));
    }

    @Override
    public String deleteClientById(Long clientId) throws NotFoundException, BadRequestException {
        Optional<ClientEntity> optionalClient = clientRepository.findById(clientId);
        if(optionalClient.isEmpty()) {
            throw new NotFoundException("Client with id = " + clientId + " not found");
        }
        ResponseEntity<Boolean> response = restTemplate.getForEntity(carEndpointUrl, Boolean.class);
        if(response.getBody()) {
            clientRepository.delete(optionalClient.get());
            return "Client with id = " + clientId + " was successfully deleted";
        } else {
            throw new BadRequestException("Can't delete client with id = " + clientId + " because they have rent or reservation");
        }
    }
}
