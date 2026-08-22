package com.matibabu.backend.patient;

import com.matibabu.backend.domain.patient.Gender;
import com.matibabu.backend.domain.patient.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.testng.AssertJUnit.assertEquals;

class PatientUuidTest {

    @Test
    void generatesUuidV7() {
        Patient patient = new Patient(
                "Test",
                "Patient",
                LocalDate.of(2000, 1, 1),
                "0700000000",
                Gender.MALE
        );

        assertEquals(7, patient.getId().version());
    }
}