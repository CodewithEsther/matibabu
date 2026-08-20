package com.matibabu.backend.domain.encounter;

import java.util.Optional;
import java.util.UUID;

public interface EncounterRepository {
    Encounter save(Encounter encounter);

    Optional<Encounter> findById(UUID id);
}
