package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;

import java.util.Optional;
import java.util.UUID;

public interface GetMedicalRecordUseCase {

    Optional<MedicalRecord> getByEncounterId(UUID encounterId);
}