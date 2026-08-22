package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListPatientsUseCase {

    Page<Patient> list(Pageable pageable);
}
