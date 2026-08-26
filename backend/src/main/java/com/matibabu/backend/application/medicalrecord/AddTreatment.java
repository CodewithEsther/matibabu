package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import com.matibabu.backend.domain.medicalrecord.Treatment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddTreatment {

    private final MedicalRecordRepository medicalRecordRepository;

    public AddTreatment(MedicalRecordRepository medicalRecordRepository) {
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

        Treatment treatment = new Treatment(
                medicalRecordId,
                description
        );

        medicalRecord.addTreatment(treatment);

        return medicalRecordRepository.save(medicalRecord);
    }
}