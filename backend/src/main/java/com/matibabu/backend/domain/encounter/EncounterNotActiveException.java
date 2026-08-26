package com.matibabu.backend.domain.encounter;

public class EncounterNotActiveException extends RuntimeException {
    public EncounterNotActiveException(String message) {
        super("Encounter is no longer active");
    }
}
