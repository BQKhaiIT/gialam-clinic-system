package com.gialamclinic.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String username;
    private String fullName;
    private String role;
}
