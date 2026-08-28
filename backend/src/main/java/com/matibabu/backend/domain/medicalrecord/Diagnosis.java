package com.matibabu.backend.domain.medicalrecord;

import java.time.Instant;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

public class Diagnosis {
    private UUID id;
    private UUID medicalRecordId;
    private String description;
    private DiagnosisType type;
    private Instant recordedAt;

    public Diagnosis(
        UUID medicalRecordId,
        String description,
        DiagnosisType type
    ) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.medicalRecordId = medicalRecordId;
        this.description = description;
        this.type = type;
        this.recordedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getMedicalRecordId() {
        return medicalRecordId;
    }


    public String getDescription() {
        return description;
    }

    public DiagnosisType getType() {
        return type;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

}
