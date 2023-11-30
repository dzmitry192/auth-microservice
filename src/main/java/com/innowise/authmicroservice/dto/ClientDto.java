package com.innowise.authmicroservice.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @NotNull(message = "Id can't be empty")
    private Long id;
    @NotBlank(message = "First name can't be empty")
    @Size(min = 2, max = 20)
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
    @Size(min = 2, max = 30)
    private String lastName;
    @Email
    @NotBlank(message = "Email can't be empty")
    private String email;
    @Size(min = 4)
    @NotBlank(message = "Password can't be empty")
    private String password;
    @NotBlank(message = "Phone number can't be empty")
    private String phoneNumber;
    @NotBlank(message = "Address can't be empty")
    private String address;
}
