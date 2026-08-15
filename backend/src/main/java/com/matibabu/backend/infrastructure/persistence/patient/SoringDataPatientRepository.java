package com.matibabu.backend.infrastructure.persistence.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SoringDataPatientRepository extends JpaRepository<PatientEntity, UUID> {
}