package com.gialamclinic.dto.response;

import com.gialamclinic.enums.AppointmentStatus;
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

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;

    private String notes;

    private AppointmentStatus status;

    private LocalDateTime createdAt;
}
