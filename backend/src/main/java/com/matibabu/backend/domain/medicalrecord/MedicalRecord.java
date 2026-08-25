package com.matibabu.backend.domain.medicalrecord;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class MedicalRecord {
    private UUID uuid;
    private UUID patientId;
    private Instant createdAt;
    
    private final List<Vital> vitals = new ArrayList<>();
    private final List<ClinicalObservation> observations = new ArrayList<>();
    private final List<Diagnosis> diagnoses = new ArrayList<>();
    private final List<Treatment> treatments = new ArrayList<>();


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

    // Vitals

    public void addVital(Vital vital) {
        vitals.add(vital);
    }

    public List<Vital> getVitals() {
        return List.copyOf(vitals);
    }

    // Clinical observations

    public void addObservation(ClinicalObservation observation) {
        observations.add(observation);
    }

    public List<ClinicalObservation> getObservations() {
        return List.copyOf(observations);
    }

    // Diagnoses

    public void addDiagnosis(Diagnosis diagnosis) {
        diagnoses.add(diagnosis);
    }

    public List<Diagnosis> getDiagnoses() {
        return List.copyOf(diagnoses);
    }

    // Treatments

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public List<Treatment> getTreatments() {
        return List.copyOf(treatments);
    }


    //Basic info

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
