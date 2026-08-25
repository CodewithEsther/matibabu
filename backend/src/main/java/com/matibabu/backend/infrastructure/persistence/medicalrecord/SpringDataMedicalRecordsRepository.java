package com.matibabu.backend.infrastructure.persistence.medicalrecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataMedicalRecordsRepository extends JpaRepository<MedicalRecordEntity, UUID> {
    List<MedicalRecordEntity> findByPatientId(UUID patientId);
}