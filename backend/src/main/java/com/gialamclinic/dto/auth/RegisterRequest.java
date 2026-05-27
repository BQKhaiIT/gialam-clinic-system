package com.gialamclinic.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min=6)

    private String password;

    @NotBlank
    private String fullName;

    @Email
    private String email;

    private String phone;

}