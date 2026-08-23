package com.matibabu.backend.application.medicalrecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecord;

import java.util.List;
import java.util.UUID;


public interface ListPatientMedicalRecordsUseCase {
    List<MedicalRecord> list(UUID patientId);
}
