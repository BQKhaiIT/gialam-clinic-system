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
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


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
    private User doctor;


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


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_activate")
    private Boolean isActive = true;

    @PrePersist
    public void prePersist(){

        this.createdAt = LocalDateTime.now();

        if(this.status == null){

            this.status = AppointmentStatus.PENDING;

        }

        if (this.isActive == null){
            this.isActive = true;
        }

    }



}