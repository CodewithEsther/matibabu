package com.matibabu.backend;

import com.matibabu.backend.domain.encounter.Encounter;
import com.matibabu.backend.domain.encounter.EncounterStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EncounterTest {

    /*
     * Verifies that a newly created Encounter starts in the ACTIVE state.
     *
     * An Encounter should have:
     * - an ID
     * - a patient ID
     * - a creation time
     * - a start time
     * - ACTIVE status
     * - no end time
     */
    @Test
    void shouldStartAsActive() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        Instant now = Instant.now();

        // Start a new encounter for the patient.
        Encounter encounter =
                Encounter.start(encounterId, now);

        // Verify that the values supplied during creation were preserved.
        assertEquals(encounterId, encounter.getId());
        assertEquals(patientId, encounter.getPatientId());

        // A newly started encounter must always be ACTIVE.
        Assertions.assertEquals(EncounterStatus.ACTIVE, encounter.getStatus());


        // An active encounter has not ended yet.
        assertNull(encounter.getEndedAt());
    }


    /*
     * Verifies the normal discharge flow:
     *
     * ACTIVE → DISCHARGED
     *
     * Discharging an encounter should also record the time
     * at which the encounter ended.
     */
    @Test
    void shouldDischargeActiveEncounter() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant startedAt =
                Instant.parse("2026-08-19T10:00:00Z");

        Instant dischargedAt =
                Instant.parse("2026-08-19T12:00:00Z");

        // Create an active encounter.
        Encounter encounter =
                Encounter.start(encounterId, startedAt);

        // Perform the domain operation.
        encounter.discharge(dischargedAt);

        // The encounter should now be DISCHARGED.
        assertEquals(
                EncounterStatus.DISCHARGED,
                encounter.getStatus()
        );

        // The discharge time should be stored as the end time.
        assertEquals(
                dischargedAt,
                encounter.getEndedAt()
        );
    }


    /*
     * Verifies the cancellation flow:
     *
     * ACTIVE → CANCELLED
     *
     * Cancellation also ends the encounter, so endedAt
     * must be populated.
     */
    @Test
    void shouldCancelActiveEncounter() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant startedAt =
                Instant.parse("2026-08-19T10:00:00Z");

        Instant cancelledAt =
                Instant.parse("2026-08-19T10:30:00Z");

        // Create an active encounter.
        Encounter encounter =
                Encounter.start(encounterId, startedAt);

        // Cancel the encounter.
        encounter.cancel(cancelledAt);

        // Verify that the status changed correctly.
        assertEquals(
                EncounterStatus.CANCELLED,
                encounter.getStatus()
        );

        // Cancellation also records when the encounter ended.
        assertEquals(
                cancelledAt,
                encounter.getEndedAt()
        );
    }


    /*
     * An encounter that has already been discharged is in a
     * terminal state.
     *
     * Therefore:
     *
     * DISCHARGED → DISCHARGED
     *
     * should not be allowed.
     *
     * This test protects one of our domain invariants.
     */
    @Test
    void shouldNotDischargeAlreadyDischargedEncounter() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant startedAt =
                Instant.parse("2026-08-19T10:00:00Z");

        Instant dischargedAt =
                Instant.parse("2026-08-19T12:00:00Z");

        // Start the encounter.
        Encounter encounter =
                Encounter.start(encounterId, startedAt);

        // Discharge it once.
        encounter.discharge(dischargedAt);

        // Attempting to discharge it again should fail.
        assertThrows(
                IllegalStateException.class,
                () -> encounter.discharge(dischargedAt)
        );
    }


    /*
     * Once an encounter has been discharged, it cannot be
     * cancelled.
     *
     * Valid:
     *
     * ACTIVE → DISCHARGED
     *
     * Invalid:
     *
     * DISCHARGED → CANCELLED
     */
    @Test
    void shouldNotCancelDischargedEncounter() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant startedAt =
                Instant.parse("2026-08-19T10:00:00Z");

        Instant dischargedAt =
                Instant.parse("2026-08-19T12:00:00Z");

        // Create the encounter.
        Encounter encounter =
                Encounter.start(encounterId, startedAt);

        // Move the encounter into its terminal DISCHARGED state.
        encounter.discharge(dischargedAt);

        // Cancellation after discharge must be rejected.
        assertThrows(
                IllegalStateException.class,
                () -> encounter.cancel(dischargedAt)
        );
    }


    /*
     * The end time can never occur before the start time.
     *
     * This protects the temporal consistency of the Encounter.
     *
     * Example of an invalid encounter:
     *
     * startedAt = 12:00
     * endedAt   = 10:00
     *
     * The domain must reject this.
     */
    @Test
    void shouldNotAllowEndTimeBeforeStartTime() {
        UUID encounterId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant startedAt =
                Instant.parse("2026-08-19T12:00:00Z");

        Instant invalidEndTime =
                Instant.parse("2026-08-19T10:00:00Z");

        // Create the encounter.
        Encounter encounter =
                Encounter.start(encounterId, startedAt);

        // Attempt to discharge using a time before the encounter started.
        assertThrows(
                IllegalArgumentException.class,
                () -> encounter.discharge(invalidEndTime)
        );
    }
}