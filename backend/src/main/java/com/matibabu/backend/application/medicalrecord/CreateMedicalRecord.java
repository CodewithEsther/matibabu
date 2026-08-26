package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateMedicalRecord {

    private final MedicalRecordRepository medicalRecordRepository;

    public CreateMedicalRecord(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord execute(UUID patientId) {
        MedicalRecord medicalRecord = new MedicalRecord(patientId);

        return medicalRecordRepository.save(medicalRecord);
    }
}
