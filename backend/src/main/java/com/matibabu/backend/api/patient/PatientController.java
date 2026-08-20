package com.matibabu.backend.api.patient;

import com.matibabu.backend.application.patient.GetPatientUseCase;
import com.matibabu.backend.application.patient.ListPatientsUseCase;
import com.matibabu.backend.application.patient.RegisterPatientUseCase;
import com.matibabu.backend.application.patient.UpdatePatientUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final RegisterPatientUseCase registerPatientUseCase;
    private final GetPatientUseCase getPatientUseCase;
    private final ListPatientsUseCase listPatientsUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;

    public PatientController(
            RegisterPatientUseCase registerPatientUseCase,
            GetPatientUseCase getPatientUseCase,
            ListPatientsUseCase listPatientsUseCase,
            UpdatePatientUseCase updatePatientUseCase
    ) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.getPatientUseCase = getPatientUseCase;
        this.listPatientsUseCase = listPatientsUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse register(
            @Valid @RequestBody RegisterPatientRequest request
    ) {
        return PatientResponse.from(
                registerPatientUseCase.register(
                        request.firstName(),
                        request.lastName(),
                        request.dateOfBirth(),
                        request.gender(),
                        request.phoneNumber(),
                        request.address()
                )
        );
    }

    @GetMapping("/{id}")
    public PatientResponse getById(@PathVariable UUID id) {
        return PatientResponse.from(
                getPatientUseCase.getById(id)
        );
    }

    @GetMapping
    public Page<PatientResponse> list(
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        return listPatientsUseCase.list(pageable)
                .map(PatientResponse::from);
    }

    @PutMapping("/{id}")
    public PatientResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientRequest request
    ) {
        return PatientResponse.from(
                updatePatientUseCase.update(
                        id,
                        request.firstName(),
                        request.lastName(),
                        request.dateOfBirth(),
                        request.gender(),
                        request.phoneNumber(),
                        request.address()
                )
        );
    }
}