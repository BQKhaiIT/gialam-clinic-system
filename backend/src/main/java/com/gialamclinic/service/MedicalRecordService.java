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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepo;
    private final AppointmentRepository appointmentRepo;
    private final MedicalRecordMapper mapper;

    //CREATE
    @Transactional
    public MedicalRecordResponse create(
            MedicalRecordRequest request
    ) {

        Appointment appointment =
                appointmentRepo
                        .findById(
                                request.getAppointmentId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Appointment not found"
                                )
                        );

        // VALIDATE APPOINTMENT DATE
        if(
                appointment.getAppointmentDate()
                        .isAfter(LocalDate.now())
        ){

            throw new BadRequestException(
                    "Chưa đến ngày khám"
            );

        }

        if (
                medicalRecordRepo
                        .existsByAppointmentId(
                                appointment.getId()
                        )
        ) {

            throw new BadRequestException(
                    "Medical record already exists"
            );

        }

        if(
                appointment.getStatus()
                        == AppointmentStatus.COMPLETED
        ){

            throw new BadRequestException(
                    "Completed appointment cannot create medical record"
            );

        }

        if(
                appointment.getStatus()
                        == AppointmentStatus.CANCELLED
        ){

            throw new BadRequestException(
                    "Cancelled appointment cannot create medical record"
            );

        }

        // CHỈ CHO PENDING HOẶC IN_PROGRESS

        if (

                appointment.getStatus()
                        != AppointmentStatus.IN_PROGRESS

        ) {

            throw new BadRequestException(

                    "Only in-progress appointments can create medical record"

            );

        }



        MedicalRecord medicalRecord =

                MedicalRecord.builder()

                        .appointment(
                                appointment
                        )

                        .patient(
                                appointment.getPatient()
                        )

                        .doctor(
                                appointment.getDoctor()
                        )

                        .diagnosis(
                                request.getDiagnosis()
                        )

                        .treatment(
                                request.getTreatment()
                        )

                        .notes(
                                request.getNotes()
                        )

                        .build();

        medicalRecord =

                medicalRecordRepo.save(
                        medicalRecord
                );

        return mapper.toResponse(

                medicalRecord

        );

    }
    //GET ALL
    public Page<MedicalRecordResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by("id").descending());
        return medicalRecordRepo.findAll(pageable).map(mapper::toResponse);
    }
}
