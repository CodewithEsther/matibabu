package com.matibabu.backend.infrastructure.persistence.medicalrecord;

import com.matibabu.backend.domain.medicalrecord.MedicalRecord;
import com.matibabu.backend.domain.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MedicalRecordRepositoryAdapter implements MedicalRecordRepository {

    private final SpringDataMedicalRecordsRepository jpaRepository;
    private final MedicalRecordMapper mapper;

    public MedicalRecordRepositoryAdapter(SpringDataMedicalRecordsRepository jpaRepository,
                                          MedicalRecordMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = mapper.toEntity(medicalRecord);
        MedicalRecordEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MedicalRecord> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<MedicalRecord> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientId(patientId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<MedicalRecord> findByEncounterId(UUID encounterId) {
        return jpaRepository.findByEncounterId(encounterId)
                .map(mapper::toDomain);
    }
}