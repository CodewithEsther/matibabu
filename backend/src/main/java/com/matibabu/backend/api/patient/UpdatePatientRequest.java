package com.matibabu.backend.api.patient;

import com.matibabu.backend.domain.patient.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record UpdatePatientRequest(

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
        String phoneNumber,

        @NotBlank
        String address
) {
}
