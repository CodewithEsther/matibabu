package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Gender;
import com.matibabu.backend.domain.patient.Patient;
import com.matibabu.backend.domain.patient.PatientRepository;

import java.time.LocalDate;
import java.util.UUID;

public class UpdatePatientService implements UpdatePatientUseCase {

    private final PatientRepository patientRepository;

    public UpdatePatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient update(
            UUID id,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            String phoneNumber,
            String address
    ) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        patient.update(
                firstName,
                lastName,
                dateOfBirth,
                gender,
                phoneNumber,
                address
        );

        return patientRepository.save(patient);
    }
}
