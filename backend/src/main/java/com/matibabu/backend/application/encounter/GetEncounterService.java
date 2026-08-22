package com.matibabu.backend.application.encounter;

import com.matibabu.backend.domain.encounter.Encounter;
import com.matibabu.backend.domain.encounter.EncounterRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetEncounterService implements GetEncounterUseCase {
//must call encounterRepository for access to storage
    private final EncounterRepository encounterRepository;

    public GetEncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    @Override
    //domain encounter class handles the logic
    public Encounter getById(UUID id) {
        return encounterRepository.findById(id)
                .orElseThrow(() -> new EncounterNotFoundException(id));
    }
}