package com.matibabu.backend.api.patient;

import com.matibabu.backend.domain.patient.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record RegisterPatientRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        @PastOrPresent
        LocalDate dateOfBirth,

        @NotNull
        Gender gender,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid (7-15 digits with optional '+' prefix)")
        String phoneNumber,

        @NotBlank
        String address
) {
}