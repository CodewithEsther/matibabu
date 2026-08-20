package com.matibabu.backend.api.exception;

import com.matibabu.backend.application.patient.DuplicatePhoneNumberException;
import com.matibabu.backend.application.patient.PatientNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(DuplicatePhoneNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicatePhoneNumber(
            DuplicatePhoneNumberException exception
    ) {
        return Map.of(
                "error", exception.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        return Map.of(
                "error", "Data integrity conflict: unique constraint violated"
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(
            ResponseStatusException exception
    ) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(Map.of("error", exception.getReason() != null ? exception.getReason() : exception.getMessage()));
    }
}