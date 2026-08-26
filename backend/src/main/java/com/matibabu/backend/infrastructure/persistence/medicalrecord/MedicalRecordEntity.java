package com.matibabu.backend.infrastructure.persistence.medicalrecord;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medicalrecords")
public class MedicalRecordEntity {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID patientId;

    // Links this medical record to the encounter
    // during which it was created.
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID encounterId;

    private Instant createdAt;

    protected MedicalRecordEntity() {
        // Required by JPA
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public void setEncounterId(UUID encounterId) {
        this.encounterId = encounterId;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getEncounterId() {
        return encounterId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}