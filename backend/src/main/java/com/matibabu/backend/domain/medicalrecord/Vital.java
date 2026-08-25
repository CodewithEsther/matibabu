package com.matibabu.backend.domain.medicalrecord;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.UUID;

public class Vital {

    private UUID id;
    private VitalType type;
    private String value;
    private String unit;
    private Instant recordedAt;

    public Vital(
            VitalType type,
            String value,
            String unit
    ) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.recordedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public VitalType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}