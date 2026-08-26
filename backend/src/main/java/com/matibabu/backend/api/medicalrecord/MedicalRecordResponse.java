package com.matibabu.backend.api.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;

import java.time.Instant;
import java.util.UUID;

public record MedicalRecordResponse(
        UUID id,
        UUID patientId,
        UUID encounterId,
        Instant createdAt
) {

    public static MedicalRecordResponse from(MedicalRecord medicalRecord) {
        return new MedicalRecordResponse(
                medicalRecord.getId(),
                medicalRecord.getPatientId(),
                medicalRecord.getEncounterId(),
                medicalRecord.getCreatedAt()
        );
    }
}