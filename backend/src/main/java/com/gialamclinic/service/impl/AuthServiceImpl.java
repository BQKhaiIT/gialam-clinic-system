package com.gialamclinic.service.impl;

import com.gialamclinic.dto.auth.LoginRequest;
import com.gialamclinic.dto.auth.LoginResponse;
import com.gialamclinic.entity.User;
import com.gialamclinic.exception.BadRequestException;
import com.gialamclinic.repository.UserRepository;
import com.gialamclinic.security.JwtService;
import com.gialamclinic.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new BadRequestException(
                                "Invalid username or password"
                        )
                );

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!matches){

            throw new BadRequestException(
                    "Invalid username or password"
            );

        }

        String token=
                jwtService.generateToken(
                        user.getUsername()
                );

        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().name()
        );

    }
}