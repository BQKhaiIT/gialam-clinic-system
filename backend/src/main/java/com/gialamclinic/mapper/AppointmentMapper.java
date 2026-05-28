package com.gialamclinic.mapper;

import com.gialamclinic.dto.response.AppointmentResponse;
import com.gialamclinic.entity.MedicalRecord;
import com.gialamclinic.entity.Appointment;

import com.gialamclinic.repository.MedicalRecordRepository;
import com.gialamclinic.repository.PrescriptionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final MedicalRecordRepository medicalRecordRepo;

    private final PrescriptionRepository prescriptionRepo;

    public AppointmentResponse toResponse(
            Appointment appointment
    ){

        boolean hasMedicalRecord =

                medicalRecordRepo
                        .existsByAppointmentId(
                                appointment.getId()
                        );

        boolean hasPrescription = false;

        if(hasMedicalRecord){

            MedicalRecord medicalRecord =

                    medicalRecordRepo
                            .findByAppointmentId(
                                    appointment.getId()
                            )
                            .orElse(null);

            if(medicalRecord != null){

                hasPrescription =

                        prescriptionRepo
                                .existsByMedicalRecordId(
                                        medicalRecord.getId()
                                );

            }

        }

        return AppointmentResponse.builder()

                .id(appointment.getId())

                .patientId(
                        appointment.getPatient().getId()
                )

                .patientName(
                        appointment.getPatient().getFullName()
                )

                .doctorId(
                        appointment.getDoctor().getId()
                )

                .doctorName(
                        appointment.getDoctor().getFullName()
                )

                .doctorSpecialization(
                        appointment.getDoctor().getSpecialization()
                )

                .doctorDepartment(
                        appointment.getDoctor().getDepartment()
                )

                .doctorPhone(
                        appointment.getDoctor().getPhone()
                )

                .doctorEmail(
                        appointment.getDoctor().getEmail()
                )

                .doctorStatus(
                        appointment.getDoctor().getStatus()
                )

                .appointmentDate(
                        appointment.getAppointmentDate()
                )

                .appointmentTime(
                        appointment.getAppointmentTime()
                )

                .reason(
                        appointment.getReason()
                )

                .notes(
                        appointment.getNotes()
                )

                .status(
                        appointment.getStatus()
                )

                .createdAt(
                        appointment.getCreatedAt()
                )

                .hasMedicalRecord(
                        hasMedicalRecord
                )

                .hasPrescription(
                        hasPrescription
                )

                .build();

    }

}