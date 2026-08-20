package com.matibabu.backend.domain.patient;

import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Patient {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;

    public Patient(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            String phoneNumber,
            String address
    ) {
        this.uuid = UuidCreator.getTimeOrderedEpoch();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    /*
     * Reconstruct an existing patient from persisted data.
     */
    public static Patient reconstitute(
            UUID uuid,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            String phoneNumber,
            String address,
            Instant createdAt,
            Instant updatedAt
    ) {
        Patient patient = new Patient(
                firstName,
                lastName,
                dateOfBirth,
                gender,
                phoneNumber,
                address
        );

        patient.uuid = uuid;
        patient.createdAt = createdAt;
        patient.updatedAt = updatedAt != null ? updatedAt : createdAt;

        return patient;
    }

    public void update(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            String phoneNumber,
            String address
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.updatedAt = Instant.now();
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

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}