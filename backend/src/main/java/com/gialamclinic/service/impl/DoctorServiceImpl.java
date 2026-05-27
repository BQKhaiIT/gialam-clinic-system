package com.gialamclinic.service.impl;

import com.gialamclinic.dto.request.DoctorRequest;
import com.gialamclinic.dto.response.DoctorResponse;
import com.gialamclinic.entity.DoctorEntity;
import com.gialamclinic.enums.DoctorStatus;
import com.gialamclinic.exception.ConflictException;
import com.gialamclinic.exception.ResourceNotFoundException;
import com.gialamclinic.mapper.DoctorMapper;
import com.gialamclinic.repository.DoctorRepository;
import com.gialamclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorResponse create(DoctorRequest request) {
        normalizeRequest(request);
        validateUniqueFieldsForCreate(request);

        DoctorEntity doctor = doctorMapper.toEntity(request);
        doctor = doctorRepository.save(doctor);

        return doctorMapper.toResponse(doctor);
    }

    @Override
    public Page<DoctorResponse> getAll(int page, int size) {
        return doctorRepository
                .findByIsActiveTrue(buildPageable(page, size))
                .map(doctorMapper::toResponse);
    }

    @Override
    public DoctorResponse getById(Long id) {
        return doctorMapper.toResponse(getDoctorById(id));
    }

    @Override
    public Page<DoctorResponse> search(
            String fullName,
            String department,
            String specialization,
            int page,
            int size
    ) {
        return doctorRepository
                .search(
                        normalizeFilter(fullName),
                        normalizeFilter(department),
                        normalizeFilter(specialization),
                        buildPageable(page, size)
                )
                .map(doctorMapper::toResponse);
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        DoctorEntity doctor = getDoctorById(id);
        normalizeRequest(request);
        validateUniqueFieldsForUpdate(id, request);

        doctorMapper.updateEntity(doctor, request);
        doctor = doctorRepository.save(doctor);

        return doctorMapper.toResponse(doctor);
    }

    @Override
    public void delete(Long id) {
        DoctorEntity doctor = getDoctorById(id);
        doctor.setIsActive(false);
        doctor.setStatus(DoctorStatus.INACTIVE);
        doctorRepository.save(doctor);
    }

    private DoctorEntity getDoctorById(Long id) {
        return doctorRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );
    }

    private void normalizeRequest(DoctorRequest request) {
        request.setFullName(cleanText(request.getFullName()));
        request.setSpecialization(cleanText(request.getSpecialization()));
        request.setPhone(cleanText(request.getPhone()));
        request.setEmail(cleanEmail(request.getEmail()));
        request.setLicenseNumber(cleanText(request.getLicenseNumber()));
        request.setAddress(cleanText(request.getAddress()));
        request.setDepartment(cleanText(request.getDepartment()));
    }

    private String cleanText(String value) {
        return value == null ? null : value.trim();
    }

    private String cleanEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    private String normalizeFilter(String value) {

        if (value == null)
            return null;

        value = value.trim();

        if (value.isBlank())
            return null;

        return value;
    }

    private void validateUniqueFieldsForCreate(DoctorRequest request) {
        if (doctorRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new ConflictException("Phone already exists");
        }

        if (doctorRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        if (doctorRepository.existsByLicenseNumberIgnoreCase(request.getLicenseNumber())) {
            throw new ConflictException("License number already exists");
        }
    }

    private void validateUniqueFieldsForUpdate(Long id, DoctorRequest request) {
        if (doctorRepository.existsByPhoneIgnoreCaseAndIdNot(request.getPhone(), id)) {
            throw new ConflictException("Phone already exists");
        }

        if (doctorRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Email already exists");
        }

        if (doctorRepository.existsByLicenseNumberIgnoreCaseAndIdNot(request.getLicenseNumber(), id)) {
            throw new ConflictException("License number already exists");
        }
    }
}
