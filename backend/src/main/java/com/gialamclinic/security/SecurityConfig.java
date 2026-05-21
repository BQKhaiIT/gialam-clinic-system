package com.gialamclinic.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
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

                .csrf(
                        csrf->csrf.disable()
                )

                .sessionManagement(
                        session->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(
                        auth->auth
                                //Auth
                                .requestMatchers(
                                        "/api/v1/auth/**"
                                )
                                .permitAll()
                                // Patient APIs
                                .requestMatchers(HttpMethod.GET,
                                        "/api/v1/patients/**")
                                .hasAnyRole("ADMIN","STAFF","DOCTOR")

                                .requestMatchers(HttpMethod.POST,
                                        "/api/v1/patients/**")
                                .hasAnyRole("ADMIN","STAFF")

                                .requestMatchers(HttpMethod.PUT,
                                        "/api/v1/patients/**")
                                .hasAnyRole("ADMIN","STAFF")

                                .requestMatchers(HttpMethod.DELETE,
                                        "/api/v1/patients/**")
                                .hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/appointments/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/appointments/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF",
                                        "DOCTOR"
                                )
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/appointments/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/medical-records/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "DOCTOR"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/medical-records/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "DOCTOR",
                                        "STAFF"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/medicines/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF"
                                )

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/medicines/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/medicines/**"
                                )
                                .hasRole(
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/medicines/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "STAFF",
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