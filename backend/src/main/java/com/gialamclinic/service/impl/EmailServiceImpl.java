package com.gialamclinic.service.impl;

import com.gialamclinic.service.EmailService;

import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.verify-url}")
    private String verifyUrl;

    @Override
    public void sendVerifyEmail(

            String email,

            String username,

            String token

    ) {

        try {

            String link =

                    verifyUrl +

                            "?token=" +

                            token;

            MimeMessage message =

                    mailSender.createMimeMessage();

            MimeMessageHelper helper =

                    new MimeMessageHelper(

                            message,

                            true,

                            "UTF-8"

                    );

            helper.setTo(email);

            helper.setSubject(

                    "GiaLam Clinic - Verify Account"

            );

            String html = """

            <div
            style="
            background:#F4FAFC;
            padding:40px;
            font-family:Arial,sans-serif;
            "
            >

            <div
            style="
            max-width:520px;
            margin:auto;
            background:white;
            border-radius:24px;
            padding:40px;
            border:1px solid #E4F1F4;
            box-shadow:
            0 20px 50px
            rgba(47,111,125,.12);
            "
            >

            <h1
            style="
            text-align:center;
            color:#2F6F7D;
            margin-bottom:8px;
            "
            >

            Gia Lam Clinic

            </h1>

            <p
            style="
            text-align:center;
            color:#64748B;
            margin-bottom:30px;
            "
            >

            Account Verification

            </p>

            <p>

            Hello <b>%s</b>

            </p>

            <p>

            Thank you for registering.

            Please verify your account
            before login.

            </p>

            <div
            style="
            text-align:center;
            margin:36px 0;
            "
            >

            <a

            href="%s"

            style="
            background:#2F6F7D;
            color:white;
            padding:14px 28px;
            border-radius:14px;
            text-decoration:none;
            font-weight:700;
            display:inline-block;
            "

            >

            Verify Account

            </a>

            </div>

            <p
            style="
            color:#94A3B8;
            font-size:13px;
            "
            >

            Verification link valid
            for 24 hours.

            </p>

            </div>

            </div>

            """.formatted(

                    username,

                    link

            );

            helper.setText(

                    html,

                    true

            );

            mailSender.send(

                    message

            );

        }

        catch (Exception exception) {

            throw new RuntimeException(

                    "Cannot send email",

                    exception

            );

        }

    }

}