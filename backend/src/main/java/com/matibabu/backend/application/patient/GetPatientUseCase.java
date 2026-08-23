package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Patient;

import java.util.UUID;

public interface GetPatientUseCase {

    Patient getById(UUID id);
}