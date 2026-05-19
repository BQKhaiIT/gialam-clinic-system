package com.gialamclinic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private LocalDate dob;

    private String gender;

    private String phone;

    private String address;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(columnDefinition = "TEXT")
    private String allergies;

}