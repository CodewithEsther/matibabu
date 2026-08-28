package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.Diagnosis;
import com.matibabu.backend.domain.medicalrecord.DiagnosisType;
import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddDiagnosis {

    private final MedicalRecordRepository medicalRecordRepository;

    public AddDiagnosis(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord execute(
            UUID medicalRecordId,
            String description,
            DiagnosisType type
    ) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Medical record not found: " + medicalRecordId
                ));

        Diagnosis diagnosis = new Diagnosis(
                medicalRecordId,
                description,
                type
        );

        medicalRecord.addDiagnosis(diagnosis);

        return medicalRecordRepository.save(medicalRecord);
    }
}
