package com.matibabu.backend.infrastructure.persistence.patient;

import com.matibabu.backend.domain.patient.Patient;
import com.matibabu.backend.domain.patient.PatientRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PatientRepositoryAdapter implements PatientRepository {

    private final SoringDataPatientRepository jpaRepository;
    private final PatientMapper mapper;

    public PatientRepositoryAdapter(SoringDataPatientRepository jpaRepository, PatientMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = mapper.toEntity(patient);
        PatientEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}