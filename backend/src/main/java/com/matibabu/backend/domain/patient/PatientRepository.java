package com.matibabu.backend.domain.patient;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository {

    Patient save(Patient patient);

    Optional<Patient> findById(UUID id);
}