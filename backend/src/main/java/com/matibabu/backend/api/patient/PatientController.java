package com.matibabu.backend.api.patient;

import com.matibabu.backend.application.patient.RegisterPatientUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final RegisterPatientUseCase registerPatientUseCase;

    public PatientController(RegisterPatientUseCase registerPatientUseCase) {
        this.registerPatientUseCase = registerPatientUseCase;
    }
}