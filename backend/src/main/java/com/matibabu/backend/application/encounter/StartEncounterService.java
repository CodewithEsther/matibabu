package com.matibabu.backend.application.encounter;

import com.matibabu.backend.domain.encounter.Encounter;
import com.matibabu.backend.domain.encounter.EncounterRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
@Service
public class StartEncounterService implements StartEncounterUseCase {
    //must call EncounterRepository for storage access
    private final EncounterRepository encounterRepository;

    public StartEncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    @Override
    //domain Encounter class handles the business logic of starting an encounter
    public Encounter start(UUID patientId, Instant now) {
        Encounter encounter = Encounter.start(patientId, now);

        return encounterRepository.save(encounter);
    }
}