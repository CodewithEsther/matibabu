package com.matibabu.backend.infrastructure.persistence.medicalrecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataMedicalRecordsRepository
        extends JpaRepository<MedicalRecordEntity, UUID> {

    // Find all medical records belonging to a patient.
    List<MedicalRecordEntity> findByPatientId(UUID patientId);

    // Find the medical record associated with an encounter.
    Optional<MedicalRecordEntity> findByEncounterId(UUID encounterId);
}