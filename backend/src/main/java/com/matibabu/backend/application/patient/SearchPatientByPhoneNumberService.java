package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Patient;
import com.matibabu.backend.domain.patient.PatientRepository;

public class SearchPatientByPhoneNumberService implements SearchPatientByPhoneNumberUseCase {

    private final PatientRepository patientRepository;

    public SearchPatientByPhoneNumberService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient searchByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with phone number: " + phoneNumber));
    }
}
