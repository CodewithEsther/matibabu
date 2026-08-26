package com.matibabu.backend.application.medicalrecord;

import java.util.UUID;

public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(UUID id) {
        super("Medical Record not found: " + id);
    }
}
