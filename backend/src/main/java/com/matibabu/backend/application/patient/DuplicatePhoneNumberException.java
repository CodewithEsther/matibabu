package com.matibabu.backend.application.patient;

public class DuplicatePhoneNumberException extends RuntimeException {

    public DuplicatePhoneNumberException(String phoneNumber) {
        super("Patient with phone number already exists: " + phoneNumber);
    }
}
