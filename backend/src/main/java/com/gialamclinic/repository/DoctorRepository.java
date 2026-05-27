package com.gialamclinic.repository;

import com.gialamclinic.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {

    Page<DoctorEntity> findByIsActiveTrue(Pageable pageable);

    Optional<DoctorEntity> findByIdAndIsActiveTrue(Long id);

    boolean existsByPhoneIgnoreCase(String phone);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    boolean existsByPhoneIgnoreCaseAndIdNot(String phone, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByLicenseNumberIgnoreCaseAndIdNot(String licenseNumber, Long id);

    @Query("""
        SELECT d
        FROM DoctorEntity d
        WHERE d.isActive = true
        AND (
            :fullName IS NULL
            OR LOWER(CAST(d.fullName AS string))
               LIKE LOWER(CONCAT('%', CAST(:fullName AS string), '%'))
        )
        AND (
            :department IS NULL
            OR LOWER(CAST(d.department AS string))
               LIKE LOWER(CONCAT('%', CAST(:department AS string), '%'))
        )
        AND (
            :specialization IS NULL
            OR LOWER(CAST(d.specialization AS string))
               LIKE LOWER(CONCAT('%', CAST(:specialization AS string), '%'))
        )
        """)
    Page<DoctorEntity> search(
            @Param("fullName") String fullName,
            @Param("department") String department,
            @Param("specialization") String specialization,
            Pageable pageable
    );
}
