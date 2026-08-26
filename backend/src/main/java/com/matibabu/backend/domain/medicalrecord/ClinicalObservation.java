package com.matibabu.backend.domain.medicalrecord;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.UUID;

public class ClinicalObservation {

    private UUID id;
    private UUID medicalRecordId;
    private String description;
    private Instant recordedAt;

    public ClinicalObservation(
        UUID medicalRecordId,
        String description) {

        this.id = UuidCreator.getTimeOrderedEpoch();
        this.medicalRecordId = medicalRecordId;
        this.description = description;
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

    public Instant getRecordedAt() {
        return recordedAt;
    }
}