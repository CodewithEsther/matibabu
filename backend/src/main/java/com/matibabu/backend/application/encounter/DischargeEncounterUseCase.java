package com.matibabu.backend.application.encounter;

import java.time.Instant;
import java.util.UUID;

public interface DischargeEncounterUseCase {

    void discharge(UUID encounterId, Instant now);
}