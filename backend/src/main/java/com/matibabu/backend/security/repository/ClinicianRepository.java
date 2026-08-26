package com.matibabu.backend.security.repository;

import com.matibabu.backend.security.entity.Clinician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClinicianRepository extends JpaRepository<Clinician, UUID> {

    Optional<Clinician> findByEmail(String email);
    boolean existsByEmail(String email);
}
