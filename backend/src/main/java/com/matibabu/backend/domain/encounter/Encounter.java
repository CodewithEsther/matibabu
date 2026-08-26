package com.matibabu.backend.domain.encounter;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Encounter {

    private final UUID id;
    private final UUID patientId;
    private final Instant startedAt;


    private EncounterStatus status;
    private Instant endedAt;

    private Encounter(
            UUID id,
            UUID patientId,
            Instant startedAt

    ) {
        this.id = Objects.requireNonNull(id, "Encounter ID cannot be null");
        this.patientId = Objects.requireNonNull(patientId, "Patient ID cannot be null");
        this.startedAt = Objects.requireNonNull(startedAt, "Start time cannot be null");


        this.status = EncounterStatus.ACTIVE;
        this.endedAt = null;
    }
  //start an encounter
    public static Encounter start( UUID patientId, Instant now) {
        UUID encounterId = UuidCreator.getTimeOrderedEpoch();
        return new Encounter(
                encounterId,
                patientId,
                now
        );
    }
    //discharge an encounter
    public void discharge(Instant now) {
        ensureActive();

        Objects.requireNonNull(now, "End time cannot be null");

        if (now.isBefore(startedAt)) {
            throw new IllegalArgumentException(
                    "End time cannot be before start time"
            );
        }

        this.status = EncounterStatus.DISCHARGED;
        this.endedAt = now;
    }
    //cancel an encounter
    //TODO handle cancellation constraints
    public void cancel(Instant now) {
        ensureActive();

        Objects.requireNonNull(now, "End time cannot be null");

        if (now.isBefore(startedAt)) {
            throw new IllegalArgumentException(
                    "End time cannot be before start time"
            );
        }

        this.status = EncounterStatus.CANCELLED;
        this.endedAt = now;
    }
    //check if a discharge is active
    private void ensureActive() {
        if (status != EncounterStatus.ACTIVE) {
            throw new EncounterNotActiveException(
                    "Encounter is no longer active"
            );
        }
    }
//getters
    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public EncounterStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }
 //if an encounter exists, reconstitute using existing data
    public static Encounter reconstitute(
            UUID id,
            UUID patientId,
            Instant startedAt,
            EncounterStatus status,
            Instant endedAt
    ) {
        Encounter encounter = new Encounter(id, patientId, startedAt);
        encounter.status = status;
        encounter.endedAt = endedAt;
        return encounter;
    }


}