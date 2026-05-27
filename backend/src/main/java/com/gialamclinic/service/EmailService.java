package com.gialamclinic.service;

public interface EmailService {

    void sendVerifyEmail(

            String email,

            String username,

            String token

    );

}