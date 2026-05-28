package com.gialamclinic.repository;

import com.gialamclinic.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Page<MedicalRecord> findByPatientIdAndIsActiveTrue (Long patientId, Pageable pageable);
    boolean existsByAppointmentId(
            Long appointmentId
    );
    Optional<MedicalRecord> findByAppointmentId(
            Long appointmentId
    );
}
