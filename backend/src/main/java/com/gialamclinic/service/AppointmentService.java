package com.gialamclinic.service;

import com.gialamclinic.dto.request.AppointmentRequest;
import com.gialamclinic.dto.request.AppointmentStatusRequest;
import com.gialamclinic.dto.response.AppointmentResponse;
import com.gialamclinic.entity.Appointment;
import com.gialamclinic.entity.DoctorEntity;
import com.gialamclinic.entity.Patient;
import com.gialamclinic.entity.User;
import com.gialamclinic.exception.ResourceNotFoundException;
import com.gialamclinic.mapper.AppointmentMapper;
import com.gialamclinic.repository.AppointmentRepository;
import com.gialamclinic.repository.DoctorRepository;
import com.gialamclinic.repository.PatientRepository;
import com.gialamclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    private final DoctorRepository doctorRepo;

    private final PatientRepository patientRepo;

    private final UserRepository userRepo;

    private final AppointmentMapper mapper;


    // CREATE

    public AppointmentResponse create(
            AppointmentRequest request
    ){

        Patient patient =
                patientRepo.findById(
                                request.getPatientId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Patient not found"
                                )
                        );

        DoctorEntity doctor =
                doctorRepo.findByIdAndIsActiveTrue(
                                request.getDoctorId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Doctor not found"
                                )
                        );


        // USER LOGIN

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
                                () -> new ResourceNotFoundException(
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


    // GET ALL

    public Page<AppointmentResponse> getAll(

            int page,
            int size
    ){

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
    ){
        Appointment appointment = getActiveAppointment(id);
        return mapper.toResponse(appointment);
    }

    public AppointmentResponse update(
            Long id,
            AppointmentRequest request
    ){
        Appointment appointment = getActiveAppointment(id);
        Patient patient = patientRepo.findById(request.getPatientId()).orElseThrow(()-> new ResourceNotFoundException("Patient not found"));
        DoctorEntity doctor = doctorRepo.findByIdAndIsActiveTrue(request.getDoctorId()).orElseThrow(()-> new ResourceNotFoundException("Doctor not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment = appointmentRepo.save(appointment);
        return mapper.toResponse(appointment);
    }

    public void delete(
            Long id
    ){

        Appointment appointment = getActiveAppointment(id);

        appointment.setIsActive(false);

        appointmentRepo.save(
                appointment
        );

    }

    public AppointmentResponse updateStatus(

            Long id,

            AppointmentStatusRequest request
    ){

        Appointment appointment = getActiveAppointment(id);


        appointment.setStatus(
                request.getStatus()
        );


        appointment =
                appointmentRepo.save(
                        appointment
                );

        return mapper.toResponse(
                appointment
        );

    }

    private Appointment getActiveAppointment(Long id) {
        return appointmentRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Appointment not found"
                        )
                );
    }

}
