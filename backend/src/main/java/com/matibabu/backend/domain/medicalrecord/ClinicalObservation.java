package com.matibabu.backend.domain.medicalrecord;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.UUID;

public class ClinicalObservation {

    private UUID id;
    private String description;
    private Instant recordedAt;

    public ClinicalObservation(String description) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.description = description;
        this.recordedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}