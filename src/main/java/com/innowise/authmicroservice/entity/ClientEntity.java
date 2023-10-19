package com.innowise.authmicroservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Entity
@Table(name = "clients")
@NoArgsConstructor
@RequiredArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 2, max = 20)
    private String firstName;
    @NonNull
    @Size(min = 2, max = 30)
    private String lastName;
    @Email
    @NonNull
    private String email;
    @Size(min = 4)
    @NonNull
    private String password;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String address;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;
}
