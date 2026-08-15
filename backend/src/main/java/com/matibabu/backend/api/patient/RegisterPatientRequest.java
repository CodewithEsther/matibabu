package com.matibabu.backend.api.patient;

import com.matibabu.backend.domain.patient.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterPatientRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        LocalDate dateOfBirth,

        @NotBlank
        String phoneNumber,

        @NotNull
        Gender gender
) {
}