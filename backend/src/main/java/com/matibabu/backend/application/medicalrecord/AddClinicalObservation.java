package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.ClinicalObservation;
import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddClinicalObservation {

    private final MedicalRecordRepository medicalRecordRepository;

    public AddClinicalObservation(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord execute(
        UUID medicalRecordId,
        String description
    ) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Medical record not found: " + medicalRecordId
            ));

        ClinicalObservation observation =
            new ClinicalObservation(medicalRecordId, description);

        medicalRecord.addObservation(observation);

        return medicalRecordRepository.save(medicalRecord);
    }
}