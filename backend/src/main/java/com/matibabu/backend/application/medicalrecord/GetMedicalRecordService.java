package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetMedicalRecordService implements GetMedicalRecordUseCase {

    private final MedicalRecordRepository medicalRecordRepository;

    public GetMedicalRecordService(
            MedicalRecordRepository medicalRecordRepository
    ) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public Optional<MedicalRecord> getByEncounterId(UUID encounterId) {

        // Find the medical record belonging to this encounter.
        return medicalRecordRepository.findByEncounterId(encounterId);
    }
}