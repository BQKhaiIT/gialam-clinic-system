package com.gialamclinic.dto.response;

import com.gialamclinic.enums.DoctorStatus;
import com.gialamclinic.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {

    private Long id;

    private String fullName;

    private String specialization;

    private String phone;

    private String email;

    private String licenseNumber;

    private Gender gender;

    private LocalDate dob;

    private String address;

    private String department;

    private DoctorStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
