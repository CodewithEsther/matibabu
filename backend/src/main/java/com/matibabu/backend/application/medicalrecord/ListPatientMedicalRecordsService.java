package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListPatientMedicalRecordsService implements ListPatientMedicalRecordsUseCase {

    private final MedicalRecordRepository medicalRecordRepository;

    public ListPatientMedicalRecordsService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public List<MedicalRecord> list(UUID patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }
}