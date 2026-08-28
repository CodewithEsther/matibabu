package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import com.matibabu.backend.domain.medicalrecord.Vital;
import com.matibabu.backend.domain.medicalrecord.VitalType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddVital {

    private final MedicalRecordRepository medicalRecordRepository;

    public AddVital(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord execute(
        UUID medicalRecordId,
        VitalType type,
        String value,
        String unit
    ) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Medical record not found: " + medicalRecordId
            ));

        Vital vital = new Vital(
            medicalRecordId,
            type,
            value,
            unit
        );

        medicalRecord.addVital(vital);

        return medicalRecordRepository.save(medicalRecord);
    }
}
