package com.innowise.authmicroservice.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "First name can't be empty")
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
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