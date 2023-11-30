package com.innowise.authmicroservice.service;

import com.innowise.authmicroservice.dto.ClientDto;
import com.innowise.authmicroservice.dto.UpdateClientDto;
import com.innowise.authmicroservice.exception.BadRequestException;
import com.innowise.authmicroservice.exception.NotFoundException;

import java.util.List;

public interface ClientService {
    List<ClientDto> getClients(Integer offset, Integer limit);
    ClientDto getClientById(Long clientId) throws NotFoundException;
    ClientDto updateClientById(Long clientId, UpdateClientDto updateClientDto) throws NotFoundException;
    String deleteClientById(Long clientId) throws NotFoundException, BadRequestException;
}
