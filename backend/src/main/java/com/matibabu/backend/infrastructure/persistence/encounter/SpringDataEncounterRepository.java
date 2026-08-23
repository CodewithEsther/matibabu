package com.matibabu.backend.infrastructure.persistence.encounter;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataEncounterRepository extends JpaRepository<EncounterEntity, UUID> {
}