package com.gialamclinic.controller;

import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.*;

import com.gialamclinic.service
        .PrescriptionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/v1/prescriptions"
)
@RequiredArgsConstructor
@CrossOrigin
public class PrescriptionController {

    private final PrescriptionService
            service;


    @GetMapping
    public ApiResponse<List<PrescriptionResponse>> getAll(){

        return ApiResponse
                .<List<PrescriptionResponse>>builder()
                .success(true)
                .message("Prescription list")
                .data(
                        service.getAll()
                )
                .build();
    }

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