package com.innowise.authmicroservice.repository;

import com.innowise.authmicroservice.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    ClientEntity findByEmail(String email);
    boolean existsByEmail(String email);
}
