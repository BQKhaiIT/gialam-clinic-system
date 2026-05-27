package com.gialamclinic.service.impl;

import com.gialamclinic.dto.auth.LoginRequest;
import com.gialamclinic.dto.auth.LoginResponse;
import com.gialamclinic.entity.User;
import com.gialamclinic.exception.BadRequestException;
import com.gialamclinic.repository.UserRepository;
import com.gialamclinic.repository.VerificationTokenRepository;
import com.gialamclinic.security.JwtService;
import com.gialamclinic.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gialamclinic.dto.auth.RegisterRequest;
import com.gialamclinic.entity.VerificationToken;
import com.gialamclinic.enums.Role;
import com.gialamclinic.repository.VerificationTokenRepository;
import com.gialamclinic.service.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final EmailService emailService;

    private final
    VerificationTokenRepository verificationTokenRepository;
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
        if(

                Boolean.FALSE.equals(

                        user.getIsActive()

                )

        ){

            throw new BadRequestException(

                    "Please verify email"

            );

        }
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
    @Override

    public void register(

            RegisterRequest request

    ){

        if(

                userRepository.existsByUsername(

                        request.getUsername()

                )

        ){

            throw new BadRequestException(

                    "Username existed"

            );

        }

        User user=

                new User();

        user.setUsername(

                request.getUsername()

        );

        user.setPassword(

                passwordEncoder.encode(

                        request.getPassword()

                )

        );

        user.setEmail(

                request.getEmail()

        );

        user.setPhone(

                request.getPhone()

        );

        user.setFullName(

                request.getFullName()

        );

        user.setRole(

                Role.ADMIN

        );

        user.setIsActive(

                false

        );

        userRepository.save(user);

        String token=

                UUID.randomUUID()

                        .toString();

        verificationTokenRepository.save(

                VerificationToken
                        .builder()

                        .token(token)

                        .user(user)

                        .expiredAt(

                                LocalDateTime.now()
                                        .plusHours(24)

                        )

                        .build()

        );

        emailService.sendVerifyEmail(

                request.getEmail(),

                request.getUsername(),

                token

        );

    }
    @Override

    public void verify(
            String token
    ){

        VerificationToken verify=

                verificationTokenRepository

                        .findByToken(token)

                        .orElseThrow(

                                ()->new BadRequestException(

                                        "Token invalid"

                                )

                        );

        if(

                verify.getExpiredAt()

                        .isBefore(

                                LocalDateTime.now()

                        )

        ){

            throw new BadRequestException(

                    "Token expired"

            );

        }

        User user=

                verify.getUser();

        user.setIsActive(true);

        userRepository.save(user);

    }
}