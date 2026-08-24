package com.matibabu.backend.domain.medicalrecord;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface MedicalRecordRepository {
    
    MedicalRecord save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findById(UUID id);
    List<MedicalRecord> findByPatientId(UUID patientId);
}
