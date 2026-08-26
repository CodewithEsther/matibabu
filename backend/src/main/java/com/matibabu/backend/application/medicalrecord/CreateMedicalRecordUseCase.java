package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;

import java.util.UUID;

public interface CreateMedicalRecordUseCase {

    MedicalRecord create(UUID encounterId);
}