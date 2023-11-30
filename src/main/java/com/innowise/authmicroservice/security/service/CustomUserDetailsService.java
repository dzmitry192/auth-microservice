package com.innowise.authmicroservice.security.service;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.repository.ClientRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) {
        ClientEntity client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new NotFoundException("Client with email = " + email + " not found");
        }
        return CustomUserDetails.build(client);
    }
}
