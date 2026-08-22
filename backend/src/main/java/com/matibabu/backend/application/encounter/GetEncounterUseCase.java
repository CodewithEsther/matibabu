package com.matibabu.backend.application.encounter;

import com.matibabu.backend.domain.encounter.Encounter;

import java.util.UUID;

public interface GetEncounterUseCase {

    Encounter getById(UUID id);
}