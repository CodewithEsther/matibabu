package com.matibabu.backend.domain.medicalrecord;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.UUID;

public class MedicalRecord {
    private UUID uuid;
    private UUID patientId;
    private Instant createdAt;
    

    public MedicalRecord(UUID patientId) {
        
        this.uuid = UuidCreator.getTimeOrderedEpoch();
        this.patientId = patientId;
        this.createdAt = Instant.now();
    }

    public static MedicalRecord reconstitute(
        UUID uuid,
        UUID patientId,
        Instant createdAt

    ){
        MedicalRecord medicalRecord = new MedicalRecord(patientId);
        medicalRecord.uuid = uuid;
        medicalRecord.createdAt = createdAt;

        return medicalRecord;

    }

    //getters

    public UUID getId(){
        return uuid;
    }
    public UUID getPatientId(){
        return patientId;
    }
    public Instant getCreatedAt(){
        return createdAt;
    }
}
