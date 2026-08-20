package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Patient;

public interface SearchPatientByPhoneNumberUseCase {

    Patient searchByPhoneNumber(String phoneNumber);
}
