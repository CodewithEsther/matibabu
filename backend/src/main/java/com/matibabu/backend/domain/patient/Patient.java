package com.matibabu.backend.domain.patient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Patient {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private Instant createdAt;
    private Gender gender;

    public Patient(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String phoneNumber,
            Gender gender
    ) {
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.createdAt = Instant.now();
    }

    /*
     * Reconstruct an existing patient from persisted data.
     */
    public static Patient reconstitute(
            UUID uuid,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String phoneNumber,
            Gender gender,
            Instant createdAt
    ) {
        Patient patient = new Patient(
                firstName,
                lastName,
                dateOfBirth,
                phoneNumber,
                gender
        );

        patient.uuid = uuid;
        patient.createdAt = createdAt;

        return patient;
    }

    // Getters

    public UUID getId() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Gender getGender() {
        return gender;
    }
}