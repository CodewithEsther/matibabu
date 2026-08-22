package com.matibabu.backend.api.exception;

import com.matibabu.backend.application.patient.PatientNotFoundException;
import com.matibabu.backend.application.encounter.EncounterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlePatientNotFound(
            PatientNotFoundException exception
    ) {
        return Map.of(
                "error", exception.getMessage()
        );
    }

    @ExceptionHandler(EncounterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEncounterNotFound(
            EncounterNotFoundException exception
    ) {
        return Map.of(
                "error", exception.getMessage()
        );
    }
}