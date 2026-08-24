package com.matibabu.backend.application.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateMedicalRecordService implements CreateMedicalRecordUseCase{
    private final MedicalRecordRepository medicalRecordRepository;

    public CreateMedicalRecordService(MedicalRecordRepository medicalRecordRepository){
        this.medicalRecordRepository = medicalRecordRepository;
    }
    @Override
    public MedicalRecord create(UUID patientId){
        MedicalRecord medicalRecord = new MedicalRecord(patientId);

        return medicalRecordRepository.save(medicalRecord);
    }
}

