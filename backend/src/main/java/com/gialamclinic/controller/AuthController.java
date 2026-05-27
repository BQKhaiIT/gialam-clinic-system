package com.gialamclinic.controller;

import com.gialamclinic.dto.auth.LoginRequest;
import com.gialamclinic.dto.auth.LoginResponse;
import com.gialamclinic.dto.auth.RegisterRequest;
import com.gialamclinic.dto.response.ApiResponse;
import com.gialamclinic.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        return new ApiResponse<>(

                true,

                "Login successful",

                authService.login(request)

        );

    }

    @PostMapping("/register")
    public ApiResponse<String> register(

            @Valid
            @RequestBody
            RegisterRequest request

    ) {

        authService.register(request);

        return new ApiResponse<>(

                true,

                "Register successful",

                "Verification email sent"

        );

    }

    @GetMapping("/verify")
    public void verify(

            @RequestParam
            String token,

            HttpServletResponse response

    ) throws IOException {

        try {

            authService.verify(token);

            response.sendRedirect(

                    "http://localhost:5173/login?verified=true"

            );

        }

        catch (Exception exception) {

            response.sendRedirect(

                    "http://localhost:5173/login?verified=false"

            );

        }

    }

}