package com.innowise.authmicroservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientDto {
    @Size(min = 2, max = 20)
    private String firstName;
    @Size(min = 2, max = 30)
    private String lastName;
    @Email
    private String email;
    @Size(min = 4)
    private String password;
    private String phoneNumber;
    private String address;
}
