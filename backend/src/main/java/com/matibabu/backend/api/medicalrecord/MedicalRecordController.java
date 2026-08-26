package com.matibabu.backend.api.medicalrecord;

import com.matibabu.backend.application.medicalrecord.AddClinicalObservation;
import com.matibabu.backend.application.medicalrecord.AddDiagnosis;
import com.matibabu.backend.application.medicalrecord.AddTreatment;
import com.matibabu.backend.application.medicalrecord.AddVital;
import com.matibabu.backend.application.medicalrecord.CreateMedicalRecord;
import com.matibabu.backend.application.medicalrecord.GetMedicalRecord;
import com.matibabu.backend.domain.medicalrecord.DiagnosisType;
import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.VitalType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final CreateMedicalRecord createMedicalRecord;
    private final GetMedicalRecord getMedicalRecord;
    private final AddVital addVital;
    private final AddClinicalObservation addClinicalObservation;
    private final AddDiagnosis addDiagnosis;
    private final AddTreatment addTreatment;

    public MedicalRecordController(
        CreateMedicalRecord createMedicalRecord,
        GetMedicalRecord getMedicalRecord,
        AddVital addVital,
        AddClinicalObservation addClinicalObservation,
        AddDiagnosis addDiagnosis,
        AddTreatment addTreatment
    ) {
        this.createMedicalRecord = createMedicalRecord;
        this.getMedicalRecord = getMedicalRecord;
        this.addVital = addVital;
        this.addClinicalObservation = addClinicalObservation;
        this.addDiagnosis = addDiagnosis;
        this.addTreatment = addTreatment;
    }

    // Create medical record
    @PostMapping
    public ResponseEntity<MedicalRecordResponse> create(
        @RequestParam UUID patientId
    ) {
        MedicalRecord medicalRecord =
            createMedicalRecord.execute(patientId);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Get medical record
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponse> getById(
            @PathVariable UUID id
    ) {
        MedicalRecord medicalRecord =
            getMedicalRecord.execute(id);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Add vital
    @PostMapping("/{id}/vitals")
    public ResponseEntity<MedicalRecordResponse> addVital(
            @PathVariable UUID id,
            @RequestParam VitalType type,
            @RequestParam String value,
            @RequestParam String unit
    ) {
        MedicalRecord medicalRecord =
            addVital.execute(id, type, value, unit);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Add clinical observation
    @PostMapping("/{id}/observations")
    public ResponseEntity<MedicalRecordResponse> addObservation(
            @PathVariable UUID id,
            @RequestParam String description
    ) {
        MedicalRecord medicalRecord =
            addClinicalObservation.execute(id, description);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Add diagnosis
    @PostMapping("/{id}/diagnoses")
    public ResponseEntity<MedicalRecordResponse> addDiagnosis(
            @PathVariable UUID id,
            @RequestParam String description,
            @RequestParam DiagnosisType type
    ) {
        MedicalRecord medicalRecord =
            addDiagnosis.execute(id, description, type);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Add treatment
    @PostMapping("/{id}/treatments")
    public ResponseEntity<MedicalRecordResponse> addTreatment(
            @PathVariable UUID id,
            @RequestParam String description
    ) {
        MedicalRecord medicalRecord =
            addTreatment.execute(id, description);

        return ResponseEntity.ok(toResponse(medicalRecord));
    }

    // Convert domain object to API response
    private MedicalRecordResponse toResponse(
            MedicalRecord medicalRecord
    ) {
        return new MedicalRecordResponse(
            medicalRecord.getId(),
            medicalRecord.getPatientId(),
            medicalRecord.getCreatedAt(),
            medicalRecord.getVitals(),
            medicalRecord.getObservations(),
            medicalRecord.getDiagnoses(),
            medicalRecord.getTreatments()
        );
    }
}