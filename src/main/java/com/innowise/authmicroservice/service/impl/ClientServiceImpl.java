package com.innowise.authmicroservice.service.impl;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<ClientEntity> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public ClientEntity getClientById(Long id) {
        return clientRepository.findById(id).get();
    }

    @Override
    public ClientEntity updateClientInfo(ClientEntity newClient, Long clientId) {
        Optional<ClientEntity> optClient = clientRepository.findById(clientId);
        if(optClient.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found!");
        }

        ClientEntity client = optClient.get();
        if(!newClient.getFirstName().isEmpty()) {
            client.setFirstName(newClient.getFirstName());
        }
        if(!newClient.getLastName().isEmpty()) {
            client.setLastName(newClient.getLastName());
        }
        if(!newClient.getPhoneNumber().isEmpty()) {
            client.setPhoneNumber(newClient.getPhoneNumber());
        }
        if(!newClient.getAddress().isEmpty()) {
            client.setAddress(newClient.getAddress());
        }

        return clientRepository.save(client);
    }

//    @Override
//    public Long deleteClientById(Long id) {
//        return id;
//    }
}
