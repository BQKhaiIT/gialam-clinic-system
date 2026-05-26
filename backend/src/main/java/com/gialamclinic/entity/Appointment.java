package com.gialamclinic.entity;

import com.gialamclinic.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    // PATIENT

    @ManyToOne
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;


    // DOCTOR

    @ManyToOne
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private DoctorEntity doctor;


    // CREATED BY

    @ManyToOne
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private User createdBy;


    @Column(name = "appointment_date")
    private LocalDate appointmentDate;


    @Column(name = "appointment_time")
    private LocalTime appointmentTime;


    @Column(columnDefinition = "TEXT")
    private String reason;


    @Column(columnDefinition = "TEXT")
    private String notes;


    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    @PrePersist
    public void prePersist(){
        super.prePersist();
        if (this.status == null) {
            this.status = AppointmentStatus.PENDING;
        }
    }

}
