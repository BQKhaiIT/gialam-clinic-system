package com.gialamclinic.mapper;

import com.gialamclinic.dto.request.DoctorRequest;
import com.gialamclinic.dto.response.DoctorResponse;
import com.gialamclinic.entity.DoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorEntity toEntity(DoctorRequest request) {
        return DoctorEntity.builder()
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .licenseNumber(request.getLicenseNumber())
                .gender(request.getGender())
                .dob(request.getDob())
                .address(request.getAddress())
                .department(request.getDepartment())
                .status(request.getStatus())
                .build();
    }

    public DoctorResponse toResponse(DoctorEntity doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .licenseNumber(doctor.getLicenseNumber())
                .gender(doctor.getGender())
                .dob(doctor.getDob())
                .address(doctor.getAddress())
                .department(doctor.getDepartment())
                .status(doctor.getStatus())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    public void updateEntity(DoctorEntity doctor, DoctorRequest request) {
        doctor.setFullName(request.getFullName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setGender(request.getGender());
        doctor.setDob(request.getDob());
        doctor.setAddress(request.getAddress());
        doctor.setDepartment(request.getDepartment());
        doctor.setStatus(request.getStatus());
    }
}
