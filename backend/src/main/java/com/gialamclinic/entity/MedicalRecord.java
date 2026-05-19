package com.gialamclinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord extends BaseEntity {
    //APPOINTMENT
    @ManyToOne
    @JoinColumn(
            name = "appointment_id",
            nullable = false
    )
    private Appointment appointment;

    //PATIENT
    @ManyToOne
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;

    //DOCTOR
    @ManyToOne
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private User doctor;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
