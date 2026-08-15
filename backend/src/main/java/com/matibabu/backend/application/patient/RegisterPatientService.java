package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Gender;
import com.matibabu.backend.domain.patient.Patient;
import com.matibabu.backend.domain.patient.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegisterPatientService implements RegisterPatientUseCase {

    private final PatientRepository patientRepository;

    public RegisterPatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient register(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String phoneNumber,
            Gender gender
    ) {
        Patient patient = new Patient(
                firstName,
                lastName,
                dateOfBirth,
                phoneNumber,
                gender
        );

        return patientRepository.save(patient);
    }
}