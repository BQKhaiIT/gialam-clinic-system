package com.gialamclinic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name="medical_record_id",
            nullable=false
    )
    private MedicalRecord medicalRecord;


    @ManyToOne
    @JoinColumn(
            name="doctor_id",
            nullable=false
    )
    private User doctor;


    @Column(columnDefinition="TEXT")
    private String note;


    @OneToMany(
            mappedBy="prescription",
            cascade=CascadeType.ALL
    )
    private List<PrescriptionDetail>
            details;

}