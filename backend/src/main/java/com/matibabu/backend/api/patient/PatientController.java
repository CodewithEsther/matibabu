package com.matibabu.backend.api.patient;

import com.matibabu.backend.application.patient.RegisterPatientUseCase;
import com.matibabu.backend.domain.patient.Patient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final RegisterPatientUseCase registerPatientUseCase;

    public PatientController(RegisterPatientUseCase registerPatientUseCase) {
        this.registerPatientUseCase = registerPatientUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient register(@Valid @RequestBody RegisterPatientRequest request) {
        return registerPatientUseCase.register(
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.phoneNumber(),
                request.gender()
        );
    }
}