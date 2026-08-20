package com.matibabu.backend.application.patient;

import com.matibabu.backend.domain.patient.Gender;
import com.matibabu.backend.domain.patient.Patient;
import com.matibabu.backend.domain.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServicesTest {

    @Mock
    private PatientRepository patientRepository;

    private RegisterPatientService registerPatientService;
    private GetPatientService getPatientService;
    private ListPatientsService listPatientsService;
    private UpdatePatientService updatePatientService;
    private DeletePatientService deletePatientService;

    @BeforeEach
    void setUp() {
        registerPatientService = new RegisterPatientService(patientRepository);
        getPatientService = new GetPatientService(patientRepository);
        listPatientsService = new ListPatientsService(patientRepository);
        updatePatientService = new UpdatePatientService(patientRepository);
        deletePatientService = new DeletePatientService(patientRepository);
    }

    @Test
    void registerPatientSavesAndReturnsPatient() {
        LocalDate dob = LocalDate.of(1995, 6, 15);
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient patient = registerPatientService.register(
                "John",
                "Kamau",
                dob,
                Gender.MALE,
                "+254712345678",
                "Nairobi"
        );

        assertNotNull(patient);
        assertEquals("John", patient.getFirstName());
        assertEquals("Kamau", patient.getLastName());
        assertEquals(Gender.MALE, patient.getGender());
        assertEquals("+254712345678", patient.getPhoneNumber());
        assertEquals("Nairobi", patient.getAddress());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void getByIdReturnsPatientWhenFound() {
        UUID id = UUID.randomUUID();
        Patient mockPatient = new Patient("John", "Kamau", LocalDate.of(1995, 6, 15), Gender.MALE, "+254712345678", "Nairobi");
        when(patientRepository.findById(id)).thenReturn(Optional.of(mockPatient));

        Patient patient = getPatientService.getById(id);

        assertNotNull(patient);
        assertEquals("John", patient.getFirstName());
        verify(patientRepository).findById(id);
    }

    @Test
    void getByIdThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> getPatientService.getById(id));
        verify(patientRepository).findById(id);
    }

    @Test
    void listReturnsPaginatedPatients() {
        Pageable pageable = PageRequest.of(0, 20);
        Patient mockPatient = new Patient("John", "Kamau", LocalDate.of(1995, 6, 15), Gender.MALE, "+254712345678", "Nairobi");
        Page<Patient> page = new PageImpl<>(List.of(mockPatient), pageable, 1);
        when(patientRepository.findAll(pageable)).thenReturn(page);

        Page<Patient> result = listPatientsService.list(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(patientRepository).findAll(pageable);
    }

    @Test
    void updateModifiesExistingPatient() {
        UUID id = UUID.randomUUID();
        Patient existing = new Patient("John", "Kamau", LocalDate.of(1995, 6, 15), Gender.MALE, "+254712345678", "Nairobi");
        when(patientRepository.findById(id)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient updated = updatePatientService.update(
                id,
                "John",
                "Kamau",
                LocalDate.of(1995, 6, 15),
                Gender.MALE,
                "+254700000000",
                "Mombasa"
        );

        assertNotNull(updated);
        assertEquals("+254700000000", updated.getPhoneNumber());
        assertEquals("Mombasa", updated.getAddress());
        verify(patientRepository).findById(id);
        verify(patientRepository).save(existing);
    }

    @Test
    void updateThrowsExceptionWhenPatientNotFound() {
        UUID id = UUID.randomUUID();
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> updatePatientService.update(
                id,
                "John",
                "Kamau",
                LocalDate.of(1995, 6, 15),
                Gender.MALE,
                "+254700000000",
                "Mombasa"
        ));
        verify(patientRepository).findById(id);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deleteRemovesPatientWhenFound() {
        UUID id = UUID.randomUUID();
        when(patientRepository.existsById(id)).thenReturn(true);

        deletePatientService.delete(id);

        verify(patientRepository).existsById(id);
        verify(patientRepository).deleteById(id);
    }

    @Test
    void deleteThrowsExceptionWhenPatientNotFound() {
        UUID id = UUID.randomUUID();
        when(patientRepository.existsById(id)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> deletePatientService.delete(id));
        verify(patientRepository).existsById(id);
        verify(patientRepository, never()).deleteById(any());
    }
}
