package com.matibabu.backend.application.patient;

import java.util.UUID;

public interface DeletePatientUseCase {

    void delete(UUID id);
}
