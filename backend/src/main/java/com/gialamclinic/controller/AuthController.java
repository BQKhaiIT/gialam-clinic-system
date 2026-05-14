package com.gialamclinic.controller;

import com.gialamclinic.dto.auth.LoginRequest;
import com.gialamclinic.dto.auth.LoginResponse;
import com.gialamclinic.response.ApiResponse;
import com.gialamclinic.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid
            @RequestBody LoginRequest request
    ) {

        return new ApiResponse<>(

                true,

                "Login successful",

                authService.login(
                        request
                )

        );

    }

}