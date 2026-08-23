package com.matibabu.backend.api.patient;

import com.matibabu.backend.application.patient.GetPatientUseCase;
import com.matibabu.backend.application.patient.RegisterPatientUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final RegisterPatientUseCase registerPatientUseCase;
    private final GetPatientUseCase getPatientUseCase;

    public PatientController(
            RegisterPatientUseCase registerPatientUseCase,
            GetPatientUseCase getPatientUseCase
    ) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.getPatientUseCase = getPatientUseCase;
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
                        request.phoneNumber(),
                        request.gender()
                )
        );
    }

    @GetMapping("/{id}")
    public PatientResponse getById(@PathVariable UUID id) {
        return PatientResponse.from(
                getPatientUseCase.getById(id)
        );
    }
}