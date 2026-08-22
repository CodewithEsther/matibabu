package com.matibabu.backend.application.encounter;

import java.time.Instant;
import java.util.UUID;

public interface CancelEncounterUseCase {

    void cancel(UUID encounterId, Instant now);
}