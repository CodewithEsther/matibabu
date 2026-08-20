package com.matibabu.backend.application.encounter;

import com.matibabu.backend.domain.encounter.Encounter;
import com.matibabu.backend.domain.encounter.EncounterRepository;

import java.time.Instant;
import java.util.UUID;

public class StartEncounterService {

    private final EncounterRepository encounterRepository;

    public StartEncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public Encounter start(UUID patientId) {

        UUID encounterId = UUID.randomUUID();
        Instant now = Instant.now();

        Encounter encounter =
                Encounter.start(encounterId, now);

        return encounterRepository.save(encounter);
    }
}