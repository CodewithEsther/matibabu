package com.matibabu.backend.api.medicalrecord;

import com.matibabu.backend.application.medicalrecord.CreateMedicalRecordUseCase;
import com.matibabu.backend.application.medicalrecord.GetMedicalRecordUseCase;
import com.matibabu.backend.application.medicalrecord.MedicalRecordNotFoundException;
import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/encounters/{encounterId}/medical-record")
public class MedicalRecordController {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final GetMedicalRecordUseCase getMedicalRecordUseCase;

    public MedicalRecordController(
            CreateMedicalRecordUseCase createMedicalRecordUseCase,
            GetMedicalRecordUseCase getMedicalRecordUseCase
    ) {
        this.createMedicalRecordUseCase = createMedicalRecordUseCase;
        this.getMedicalRecordUseCase = getMedicalRecordUseCase;
    }

    /*
     * Creates a medical record for an existing encounter.
     *
     * The encounterId comes from the URL.
     * The service is responsible for finding the encounter
     * and obtaining the patientId associated with it.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponse create(
            @PathVariable UUID encounterId
    ) {
        MedicalRecord medicalRecord =
                createMedicalRecordUseCase.create(encounterId);

        return MedicalRecordResponse.from(medicalRecord);
    }

    /*
     * Retrieves the medical record belonging to an encounter.
     *
     * The repository returns Optional because an encounter
     * may exist without having a medical record yet.
     */
    @GetMapping
    public MedicalRecordResponse get(
            @PathVariable UUID encounterId
    ) {
        return getMedicalRecordUseCase
                .getByEncounterId(encounterId)
                .map(MedicalRecordResponse::from)
                .orElseThrow(
                        () -> new MedicalRecordNotFoundException(encounterId)
                );
    }
}

