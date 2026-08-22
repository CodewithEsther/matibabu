package com.matibabu.backend.application.encounter;

import java.util.UUID;

public class EncounterNotFoundException extends RuntimeException {

    public EncounterNotFoundException(UUID id) {
        super("Encounter not found: " + id);
    }
}