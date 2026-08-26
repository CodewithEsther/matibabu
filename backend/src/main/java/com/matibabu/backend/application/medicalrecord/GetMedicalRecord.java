package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetMedicalRecord {

    private final MedicalRecordRepository medicalRecordRepository;

    public GetMedicalRecord(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord execute(UUID medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Medical record not found: " + medicalRecordId
                ));
    }
}