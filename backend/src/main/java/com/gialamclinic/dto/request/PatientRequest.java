package com.gialamclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    private LocalDate dob;

    private String gender;

    private String phone;

    private String address;

    private String bloodType;

    private String allergies;

}