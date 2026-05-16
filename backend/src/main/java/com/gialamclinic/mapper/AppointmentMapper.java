package com.gialamclinic.mapper;

import com.gialamclinic.dto.response.AppointmentResponse;
import com.gialamclinic.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(
            Appointment appointment
    ){

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

                .build();

    }

}