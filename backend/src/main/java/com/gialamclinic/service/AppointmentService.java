package com.gialamclinic.service;

import com.gialamclinic.dto.request.AppointmentRequest;
import com.gialamclinic.dto.request.AppointmentStatusRequest;
import com.gialamclinic.dto.response.AppointmentResponse;

import com.gialamclinic.entity.*;

import com.gialamclinic.enums.AppointmentStatus;

import com.gialamclinic.exception.BadRequestException;
import com.gialamclinic.exception.ConflictException;
import com.gialamclinic.exception.ResourceNotFoundException;

import com.gialamclinic.mapper.AppointmentMapper;

import com.gialamclinic.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    private final DoctorRepository doctorRepo;

    private final PatientRepository patientRepo;

    private final UserRepository userRepo;

    private final MedicalRecordRepository medicalRecordRepo;

    private final PrescriptionRepository prescriptionRepo;

    private final AppointmentMapper mapper;

    public AppointmentResponse create(
            AppointmentRequest request
    ) {

        validateWorkingHours(request);

        validateDuplicateAppointment(
                request
        );

        Patient patient =
                patientRepo.findById(
                                request.getPatientId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Patient not found"
                                        )
                        );

        DoctorEntity doctor =
                doctorRepo
                        .findByIdAndIsActiveTrue(
                                request.getDoctorId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Doctor not found"
                                        )
                        );

        String username =

                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User createdBy =
                userRepo.findByUsername(
                                username
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User not found"
                                        )
                        );

        Appointment appointment =
                Appointment.builder()

                        .patient(patient)

                        .doctor(doctor)

                        .createdBy(createdBy)

                        .appointmentDate(
                                request.getAppointmentDate()
                        )

                        .appointmentTime(
                                request.getAppointmentTime()
                        )

                        .reason(
                                request.getReason()
                        )

                        .notes(
                                request.getNotes()
                        )

                        .build();

        appointment =
                appointmentRepo.save(
                        appointment
                );

        return mapper.toResponse(
                appointment
        );

    }

    public Page<AppointmentResponse> getAll(

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(

                        page,

                        size,

                        Sort.by("id")
                                .descending()

                );

        return appointmentRepo
                .findByIsActiveTrue(pageable)
                .map(mapper::toResponse);

    }

    public AppointmentResponse getById(
            Long id
    ) {

        return mapper.toResponse(
                getActiveAppointment(id)
        );

    }

    public AppointmentResponse update(

            Long id,

            AppointmentRequest request

    ) {

        Appointment appointment =
                getActiveAppointment(id);

        validateWorkingHours(request);

        boolean changedSchedule =

                !appointment
                        .getDoctor()
                        .getId()
                        .equals(
                                request.getDoctorId()
                        )

                        ||

                        !appointment
                                .getPatient()
                                .getId()
                                .equals(
                                        request.getPatientId()
                                )

                        ||

                        !appointment
                                .getAppointmentDate()
                                .equals(
                                        request.getAppointmentDate()
                                )

                        ||

                        !appointment
                                .getAppointmentTime()
                                .equals(
                                        request.getAppointmentTime()
                                );

        if (changedSchedule) {

            validateDuplicateAppointment(
                    request
            );

        }

        Patient patient =
                patientRepo.findById(
                                request.getPatientId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Patient not found"
                                        )
                        );

        DoctorEntity doctor =
                doctorRepo
                        .findByIdAndIsActiveTrue(
                                request.getDoctorId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Doctor not found"
                                        )
                        );

        appointment.setPatient(patient);

        appointment.setDoctor(doctor);

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setNotes(
                request.getNotes()
        );

        appointment =
                appointmentRepo.save(
                        appointment
                );

        return mapper.toResponse(
                appointment
        );

    }

    public void delete(
            Long id
    ) {

        Appointment appointment =
                getActiveAppointment(id);

        appointment.setIsActive(false);

        appointmentRepo.save(
                appointment
        );

    }

    public AppointmentResponse updateStatus(

            Long id,

            AppointmentStatusRequest request

    ) {

        Appointment appointment =
                getActiveAppointment(id);

        AppointmentStatus currentStatus =
                appointment.getStatus();

        AppointmentStatus newStatus =
                request.getStatus();

        // COMPLETED không cho đổi nữa

        if (currentStatus == AppointmentStatus.COMPLETED) {

            throw new BadRequestException(
                    "Appointment already completed"
            );

        }

        // VALIDATE FLOW

        boolean validTransition =

                (currentStatus == AppointmentStatus.PENDING
                        && newStatus == AppointmentStatus.CONFIRMED)

                        ||

                        (currentStatus == AppointmentStatus.CONFIRMED
                                && newStatus == AppointmentStatus.IN_PROGRESS)

                        ||

                        (currentStatus == AppointmentStatus.PENDING
                                && newStatus == AppointmentStatus.CANCELLED)

                        ||

                        (currentStatus == AppointmentStatus.CONFIRMED
                                && newStatus == AppointmentStatus.CANCELLED);

        if (!validTransition) {

            throw new BadRequestException(
                    "Invalid appointment status transition"
            );

        }

        // VALIDATE COMPLETE

        if (newStatus == AppointmentStatus.COMPLETED) {

            // CHECK MEDICAL RECORD

            boolean hasMedicalRecord =

                    medicalRecordRepo
                            .existsByAppointmentId(
                                    appointment.getId()
                            );

            if (!hasMedicalRecord) {

                throw new BadRequestException(
                        "Chưa tạo hồ sơ khám bệnh"
                );

            }

            // GET MEDICAL RECORD

            MedicalRecord medicalRecord =

                    medicalRecordRepo
                            .findByAppointmentId(
                                    appointment.getId()
                            )
                            .orElseThrow(
                                    () -> new BadRequestException(
                                            "Medical record not found"
                                    )
                            );

            // CHECK PRESCRIPTION

            boolean hasPrescription =

                    prescriptionRepo
                            .existsByMedicalRecordId(
                                    medicalRecord.getId()
                            );

            if (!hasPrescription) {

                throw new BadRequestException(
                        "Chưa tạo đơn thuốc"
                );

            }

        }

        appointment.setStatus(
                newStatus
        );

        appointment =
                appointmentRepo.save(
                        appointment
                );

        return mapper.toResponse(
                appointment
        );

    }

    private Appointment getActiveAppointment(
            Long id
    ) {

        return appointmentRepo
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                )
                );

    }

    private void validateWorkingHours(
            AppointmentRequest request
    ) {

        if (

                request
                        .getAppointmentDate()
                        .getDayOfWeek()

                        ==

                        DayOfWeek.SUNDAY

        ) {

            throw new BadRequestException(

                    "Phòng khám nghỉ Chủ nhật"

            );

        }

        LocalTime time =
                request.getAppointmentTime();

        boolean morning =

                !time.isBefore(
                        LocalTime.of(8, 0)
                )

                        &&

                        !time.isAfter(
                                LocalTime.of(12, 0)
                        );

        boolean afternoon =

                !time.isBefore(
                        LocalTime.of(13, 30)
                )

                        &&

                        !time.isAfter(
                                LocalTime.of(17, 30)
                        );

        if (

                !morning

                        &&

                        !afternoon

        ) {

            throw new BadRequestException(

                    "Giờ khám 08:00-12:00 hoặc 13:30-17:30"

            );

        }

    }

    private void validateDuplicateAppointment(

            AppointmentRequest request

    ) {

        List<AppointmentStatus> statuses =

                List.of(

                        AppointmentStatus.PENDING,

                        AppointmentStatus.CONFIRMED,

                        AppointmentStatus.IN_PROGRESS

                );

        boolean doctorBusy =

                appointmentRepo
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusInAndIsActiveTrue(

                                request.getDoctorId(),

                                request.getAppointmentDate(),

                                request.getAppointmentTime(),

                                statuses

                        );

        if (doctorBusy) {

            throw new ConflictException(

                    "Bác sĩ đã có lịch khám"

            );

        }

        boolean patientBusy =

                appointmentRepo
                        .existsByPatientIdAndAppointmentDateAndAppointmentTimeAndStatusInAndIsActiveTrue(

                                request.getPatientId(),

                                request.getAppointmentDate(),

                                request.getAppointmentTime(),

                                statuses

                        );

        if (patientBusy) {

            throw new ConflictException(

                    "Bệnh nhân đã có lịch khám"

            );

        }

    }

}