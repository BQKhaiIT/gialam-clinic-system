package com.gialamclinic.repository;

import com.gialamclinic.entity.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionDetailRepository
        extends JpaRepository<
        PrescriptionDetail,
        Long
        > {

}