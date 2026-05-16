package com.gialamclinic.repository;

import com.gialamclinic.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByIsActiveTrue(Pageable pageable);

    Page<Patient>
    findByFullNameContainingIgnoreCaseAndIsActiveTrueOrPhoneContainingAndIsActiveTrue(
            String fullName,
            String phone,
            Pageable pageable
    );

}