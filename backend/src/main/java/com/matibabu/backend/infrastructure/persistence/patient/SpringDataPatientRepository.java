package com.matibabu.backend.infrastructure.persistence.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPatientRepository extends JpaRepository<PatientEntity, UUID> {

    Optional<PatientEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}