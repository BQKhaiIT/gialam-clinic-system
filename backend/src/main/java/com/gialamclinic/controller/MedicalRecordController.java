package com.gialamclinic.controller;

import com.gialamclinic.dto.request.MedicalRecordRequest;
import com.gialamclinic.dto.response.*;
import com.gialamclinic.entity.MedicalRecord;
import com.gialamclinic.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@CrossOrigin

public class MedicalRecordController {
    private final MedicalRecordService service;

    //CREATE
    @PostMapping
    public ApiResponse<MedicalRecordResponse> create(
            @RequestBody
            @Valid MedicalRecordRequest request
    ) {
        MedicalRecordResponse response = service.create(request);
        return ApiResponse
                .<MedicalRecordResponse>builder()
                .success(true)
                .message("Medical record created successfully!")
                .data(response)
                .build();
    }
    //GET ALL
    @GetMapping
    public ApiResponse<Page<MedicalRecordResponse>> getAll(
            @RequestParam(
                    defaultValue = "0"
            )
            int page,
            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ){
        Page<MedicalRecordResponse> response = service.getAll(page, size);
        return ApiResponse
                .<Page<MedicalRecordResponse>>builder()
                .success(true)
                .message("Medical record list fetched successfully!")
                .data(response)
                .build();
    }
}
