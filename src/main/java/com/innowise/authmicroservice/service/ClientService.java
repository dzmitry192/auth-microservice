package com.innowise.authmicroservice.service;

import com.innowise.authmicroservice.entity.ClientEntity;

import java.util.List;

public interface ClientService {

    List<ClientEntity> getClients();

    ClientEntity getClientById(Long id);

    ClientEntity updateClientInfo(ClientEntity client, Long clientId);

//    Long deleteClientById(Long id);
}
