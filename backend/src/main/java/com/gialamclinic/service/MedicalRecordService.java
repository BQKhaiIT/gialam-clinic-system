package com.gialamclinic.service;

import com.gialamclinic.dto.request.MedicalRecordRequest;
import com.gialamclinic.dto.response.MedicalRecordResponse;
import com.gialamclinic.entity.*;
import com.gialamclinic.enums.AppointmentStatus;
import com.gialamclinic.exception.BadRequestException;
import com.gialamclinic.exception.ResourceNotFoundException;
import com.gialamclinic.mapper.MedicalRecordMapper;
import com.gialamclinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepo;
    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final MedicalRecordMapper mapper;

    //CREATE
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepo.findById(request.getAppointmentId()).orElseThrow(()->new ResourceNotFoundException("Appointment not found"));

        //BUSINESS VALIDATION
        if (
                appointment.getStatus()!=AppointmentStatus.COMPLETED
        ) {
            throw new BadRequestException("Medical record can only be created when appointment is COMPLETED");
        }
        //USER LOGIN
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User doctor = userRepo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("Doctor not found"));
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .doctor(doctor)
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .build();
        medicalRecord = medicalRecordRepo.save(medicalRecord);
        return mapper.toResponse(medicalRecord);
    }
    //GET ALL
    public Page<MedicalRecordResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by("id").descending());
        return medicalRecordRepo.findAll(pageable).map(mapper::toResponse);
    }
}
