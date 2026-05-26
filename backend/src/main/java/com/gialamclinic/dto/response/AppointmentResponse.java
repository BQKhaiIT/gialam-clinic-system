package com.gialamclinic.dto.response;

import com.gialamclinic.enums.AppointmentStatus;
import com.gialamclinic.enums.DoctorStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private String doctorDepartment;
    private String doctorPhone;
    private String doctorEmail;
    private DoctorStatus doctorStatus;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;

    private String notes;

    private AppointmentStatus status;

    private LocalDateTime createdAt;
}
