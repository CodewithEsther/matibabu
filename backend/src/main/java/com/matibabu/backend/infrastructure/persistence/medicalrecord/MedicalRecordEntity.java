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

    private Instant createdAt;

    protected MedicalRecordEntity(){
        //required by jpa
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public void setStartedAt(Instant startedAt) {
        this.createdAt = createdAt;
    }


    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
