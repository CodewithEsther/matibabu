package com.matibabu.backend.domain.medicalrecord;

import java.time.Instant;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

public class Diagnosis {
    private UUID id;
    private String description;
    private DiagnosisType type;
    private Instant recordedAt;

    public Diagnosis(
            String description,
            DiagnosisType type
    ) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.description = description;
        this.type = type;
        this.recordedAt = Instant.now();
    }

    public UUID getId() {
        return id;
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
