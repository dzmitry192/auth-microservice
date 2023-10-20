package com.innowise.authmicroservice.controller;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.service.impl.ClientServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ClientController {

    private final ClientServiceImpl clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> getClients() {
        return ResponseEntity.ok().body(clientService.getClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long clientId) {
        return ResponseEntity.ok().body(clientService.getClientById(clientId));
    }

    @PatchMapping("/{clientId}")
    public ResponseEntity<ClientEntity> updateClientInfo(@Valid @RequestBody ClientEntity client, @PathVariable Long clientId) {
        return ResponseEntity.ok().body(clientService.updateClientInfo(client, clientId));
    }

//    @DeleteMapping("/{clientId}")
//    public ResponseEntity<Long> deleteClientById(@PathVariable Long clientId) {
//        return ResponseEntity.ok().body(clientService.deleteClientById(clientId));
//    }
}
