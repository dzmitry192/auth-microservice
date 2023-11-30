package com.innowise.authmicroservice.controller;

import com.innowise.authmicroservice.dto.ClientDto;
import com.innowise.authmicroservice.dto.UpdateClientDto;
import com.innowise.authmicroservice.exception.BadRequestException;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.service.impl.ClientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientServiceImpl clientService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<List<ClientDto>> getClients(@RequestParam Integer offset, @RequestParam Integer limit) {
        return ResponseEntity.ok().body(clientService.getClients(offset, limit));
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long clientId) throws NotFoundException {
        return ResponseEntity.ok().body(clientService.getClientById(clientId));
    }

    @PatchMapping("/{clientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<ClientDto> updateClientById(@PathVariable Long clientId, @Valid @RequestBody UpdateClientDto updateClientDto) throws NotFoundException {
        return ResponseEntity.ok().body(clientService.updateClientById(clientId, updateClientDto));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<String> deleteClientId(@PathVariable Long clientId) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(clientService.deleteClientById(clientId));
    }
}