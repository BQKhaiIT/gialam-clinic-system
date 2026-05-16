package com.gialamclinic.service;

import com.gialamclinic.dto.request.PatientRequest;
import com.gialamclinic.dto.response.PatientResponse;
import com.gialamclinic.entity.Patient;
import com.gialamclinic.mapper.PatientMapper;
import com.gialamclinic.repository.PatientRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.gialamclinic.exception.ResourceNotFoundException;

@Service
public class PatientService {

    private final PatientRepository repo;
    private final PatientMapper mapper;

    public PatientService(
            PatientRepository repo,
            PatientMapper mapper){

        this.repo = repo;
        this.mapper = mapper;
    }

    // CREATE

    public PatientResponse create(PatientRequest request){

        Patient patient = mapper.toEntity(request);

        patient = repo.save(patient);

        return mapper.toResponse(patient);
    }


    // GET ALL + PAGINATION

    public Page<PatientResponse> getAll(
            int page,
            int size){

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        return repo.findByIsActiveTrue(pageable)
                .map(mapper::toResponse);
    }


    // SEARCH

    public Page<PatientResponse> search(
            String keyword,
            int page,
            int size){

        Pageable pageable = PageRequest.of(
                page,
                size
        );

        return repo
                .findByFullNameContainingIgnoreCaseAndIsActiveTrueOrPhoneContainingAndIsActiveTrue(
                        keyword,
                        keyword,
                        pageable
                )
                .map(mapper::toResponse);
    }


    // UPDATE

    public PatientResponse update(
            Long id,
            PatientRequest request){

        Patient patient =
                repo.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Patient not found"
                                )
                        );

        mapper.updateEntity(
                patient,
                request
        );

        patient = repo.save(patient);

        return mapper.toResponse(patient);
    }


    // SOFT DELETE

    public void delete(Long id){

        Patient patient =
                repo.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Patient not found"
                                )
                        );

        patient.setIsActive(false);

        repo.save(patient);

    }

}