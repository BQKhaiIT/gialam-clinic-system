package com.gialamclinic.dto.request;

import com.gialamclinic.enums.DoctorStatus;
import com.gialamclinic.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name must not exceed 120 characters")
    private String fullName;

    @NotBlank(message = "Specialization is required")
    @Size(max = 120, message = "Specialization must not exceed 120 characters")
    private String specialization;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^[0-9+()\\-\\s]{8,20}$",
            message = "Phone must be between 8 and 20 characters and contain only valid phone characters"
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "License number is required")
    @Pattern(
            regexp = "^[A-Za-z0-9/_\\-]{3,60}$",
            message = "License number must be 3 to 60 characters and contain only letters, numbers, /, _, or -"
    )
    private String licenseNumber;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Department is required")
    @Size(max = 120, message = "Department must not exceed 120 characters")
    private String department;

    @NotNull(message = "Status is required")
    private DoctorStatus status;
}
