package com.gialamclinic.service;

import com.gialamclinic.dto.request.DoctorRequest;
import com.gialamclinic.dto.response.DoctorResponse;
import org.springframework.data.domain.Page;

public interface DoctorService {

    DoctorResponse create(DoctorRequest request);

    Page<DoctorResponse> getAll(int page, int size);

    DoctorResponse getById(Long id);

    Page<DoctorResponse> search(
            String fullName,
            String department,
            String specialization,
            int page,
            int size
    );

    DoctorResponse update(Long id, DoctorRequest request);

    void delete(Long id);
}
