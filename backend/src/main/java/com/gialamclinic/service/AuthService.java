package com.gialamclinic.service;

import com.gialamclinic.dto.auth.LoginRequest;
import com.gialamclinic.dto.auth.LoginResponse;
import com.gialamclinic.dto.auth.RegisterRequest;

public interface AuthService {

    LoginResponse login(
            LoginRequest request
    );

    void register(
            RegisterRequest request
    );

    void verify(
            String token
    );

}