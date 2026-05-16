package com.gialamclinic.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;

    private String fullName;

    private LocalDate dob;

    private String gender;

    private String phone;

    private String address;

    private String bloodType;

    private String allergies;

    private Boolean isActive;

    private LocalDateTime createdAt;

}