package com.gialamclinic.controller;

import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.*;

import com.gialamclinic.service
        .PrescriptionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "/api/v1/prescriptions"
)
@RequiredArgsConstructor
@CrossOrigin
public class PrescriptionController {

    private final PrescriptionService
            service;


    @PostMapping
    public ApiResponse<PrescriptionResponse>
    create(

            @RequestBody
            @Valid
            PrescriptionRequest request

    ){

        return ApiResponse

                .<PrescriptionResponse>
                        builder()

                .success(true)

                .message(
                        "Prescription created"
                )

                .data(

                        service.create(
                                request
                        )

                )

                .build();

    }

}