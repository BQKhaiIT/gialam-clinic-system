package com.gialamclinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="prescription_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDetail
        extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name="prescription_id",
            nullable=false
    )
    private Prescription prescription;


    @ManyToOne
    @JoinColumn(
            name="medicine_id",
            nullable=false
    )
    private Medicine medicine;


    private Integer quantity;


    private String dosage;


    private String instruction;

}