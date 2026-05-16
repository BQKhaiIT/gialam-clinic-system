package com.gialamclinic.mapper;

import com.gialamclinic.dto.request.PatientRequest;
import com.gialamclinic.dto.response.PatientResponse;
import com.gialamclinic.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest request){

        return Patient.builder()
                .fullName(request.getFullName())
                .dob(request.getDob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .address(request.getAddress())
                .bloodType(request.getBloodType())
                .allergies(request.getAllergies())
                .build();
    }

    public PatientResponse toResponse(Patient patient){

        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dob(patient.getDob())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .bloodType(patient.getBloodType())
                .allergies(patient.getAllergies())
                .isActive(patient.getIsActive())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    public void updateEntity(
            Patient patient,
            PatientRequest request){

        patient.setFullName(request.getFullName());
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setBloodType(request.getBloodType());
        patient.setAllergies(request.getAllergies());

    }

}