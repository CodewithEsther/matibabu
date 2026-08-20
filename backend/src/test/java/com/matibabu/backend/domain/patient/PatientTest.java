package com.matibabu.backend.domain.patient;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void createsPatientWithGeneratedUuidAndTimestamps() {
        LocalDate dob = LocalDate.of(1995, 6, 15);
        Patient patient = new Patient("John", "Kamau", dob, Gender.MALE, "+254712345678", "Nairobi");

        assertNotNull(patient.getId());
        assertEquals(7, patient.getId().version());
        assertEquals("John", patient.getFirstName());
        assertEquals("Kamau", patient.getLastName());
        assertEquals(dob, patient.getDateOfBirth());
        assertEquals(Gender.MALE, patient.getGender());
        assertEquals("+254712345678", patient.getPhoneNumber());
        assertEquals("Nairobi", patient.getAddress());
        assertNotNull(patient.getCreatedAt());
        assertNotNull(patient.getUpdatedAt());
        assertEquals(patient.getCreatedAt(), patient.getUpdatedAt());
    }

    @Test
    void updatesDemographicInformationAndRefreshesUpdatedAt() throws InterruptedException {
        LocalDate dob = LocalDate.of(1995, 6, 15);
        Patient patient = new Patient("John", "Kamau", dob, Gender.MALE, "+254712345678", "Nairobi");
        UUID id = patient.getId();
        Instant initialCreatedAt = patient.getCreatedAt();
        Instant initialUpdatedAt = patient.getUpdatedAt();

        Thread.sleep(10);

        patient.update("John", "Kamau", dob, Gender.MALE, "+254700000000", "Mombasa");

        assertEquals(id, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Kamau", patient.getLastName());
        assertEquals("+254700000000", patient.getPhoneNumber());
        assertEquals("Mombasa", patient.getAddress());
        assertEquals(initialCreatedAt, patient.getCreatedAt());
        assertTrue(patient.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    void reconstitutesPatient() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T00:00:00Z");
        LocalDate dob = LocalDate.of(1990, 1, 1);

        Patient patient = Patient.reconstitute(
                id,
                "Jane",
                "Doe",
                dob,
                Gender.FEMALE,
                "+254711111111",
                "Kisumu",
                createdAt,
                updatedAt
        );

        assertEquals(id, patient.getId());
        assertEquals("Jane", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(dob, patient.getDateOfBirth());
        assertEquals(Gender.FEMALE, patient.getGender());
        assertEquals("+254711111111", patient.getPhoneNumber());
        assertEquals("Kisumu", patient.getAddress());
        assertEquals(createdAt, patient.getCreatedAt());
        assertEquals(updatedAt, patient.getUpdatedAt());
    }
}
