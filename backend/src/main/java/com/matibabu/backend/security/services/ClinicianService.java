package com.matibabu.backend.security.services;

import com.matibabu.backend.security.entity.Clinician;
import com.matibabu.backend.security.dto.RegistrationRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ClinicianService {
    Clinician registerClinician(RegistrationRequest registrationRequest);
    Clinician promoteToAdmin(UUID id);
}
