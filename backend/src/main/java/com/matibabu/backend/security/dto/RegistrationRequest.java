package com.matibabu.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank
        @Email(message = "Invalid email")
        String email,

        @NotBlank
        @Size(min = 8, max = 100, message = "Password must be atleast 8 characters long")
        String password) {

}
