package com.gialamclinic.repository;

import com.gialamclinic.entity.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository
        extends JpaRepository<
        Prescription,
        Long
        > {

    boolean existsByMedicalRecordId(
            Long medicalRecordId
    );

}