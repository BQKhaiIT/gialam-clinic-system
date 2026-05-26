package com.gialamclinic.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http

                .cors(Customizer.withDefaults())

                .csrf(
                        csrf -> csrf.disable()
                )

                .sessionManagement(
                        session->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(
                        auth->auth
                                .requestMatchers(
                                        HttpMethod.OPTIONS,
                                        "/**"
                                )
                                .permitAll()
                                //Auth
                                .requestMatchers(
                                        "/api/v1/auth/**"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/doctors/**",
                                        "/patients/**",
                                        "/appointments/**",
                                        "/medicines/**",
                                        "/medical-records/**",
                                        "/api/v1/doctors/**",
                                        "/api/v1/patients/**",
                                        "/api/v1/appointments/**",
                                        "/api/v1/medicines/**",
                                        "/api/v1/medical-records/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/prescriptions/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "DOCTOR"
                                )

                                .anyRequest()
                                .authenticated()
                )

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class

                )

                .build();

    }

}
