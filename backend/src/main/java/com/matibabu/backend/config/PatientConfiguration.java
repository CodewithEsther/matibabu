package com.matibabu.backend.config;

import com.matibabu.backend.application.patient.GetPatientService;
import com.matibabu.backend.application.patient.GetPatientUseCase;
import com.matibabu.backend.application.patient.RegisterPatientService;
import com.matibabu.backend.application.patient.RegisterPatientUseCase;
import com.matibabu.backend.domain.patient.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientConfiguration {

    @Bean
    public RegisterPatientUseCase registerPatientUseCase(
            PatientRepository patientRepository
    ) {
        return new RegisterPatientService(patientRepository);
    }

    @Bean
    public GetPatientUseCase getPatientUseCase(
            PatientRepository patientRepository
    ) {
        return new GetPatientService(patientRepository);
    }
}