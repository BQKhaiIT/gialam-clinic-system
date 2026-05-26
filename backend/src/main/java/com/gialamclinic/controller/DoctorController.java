package com.gialamclinic.controller;

import com.gialamclinic.dto.request.DoctorRequest;
import com.gialamclinic.dto.response.ApiResponse;
import com.gialamclinic.dto.response.DoctorResponse;
import com.gialamclinic.service.DoctorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ApiResponse<DoctorResponse> create(
            @RequestBody
            @Valid
            DoctorRequest request
    ) {
        DoctorResponse response = doctorService.create(request);

        return ApiResponse
                .<DoctorResponse>builder()
                .success(true)
                .message("Doctor created successfully!")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<DoctorResponse>> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size
    ) {
        Page<DoctorResponse> response = doctorService.getAll(page, size);

        return ApiResponse
                .<Page<DoctorResponse>>builder()
                .success(true)
                .message("Doctor list fetched successfully!")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DoctorResponse> getById(
            @PathVariable
            Long id
    ) {
        DoctorResponse response = doctorService.getById(id);

        return ApiResponse
                .<DoctorResponse>builder()
                .success(true)
                .message("Doctor fetched successfully!")
                .data(response)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<DoctorResponse>> search(
            @RequestParam(required = false)
            String fullName,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String specialization,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size
    ) {
        Page<DoctorResponse> response = doctorService.search(
                fullName,
                department,
                specialization,
                page,
                size
        );

        return ApiResponse
                .<Page<DoctorResponse>>builder()
                .success(true)
                .message("Doctor search completed successfully!")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DoctorResponse> update(
            @PathVariable
            Long id,

            @RequestBody
            @Valid
            DoctorRequest request
    ) {
        DoctorResponse response = doctorService.update(id, request);

        return ApiResponse
                .<DoctorResponse>builder()
                .success(true)
                .message("Doctor updated successfully!")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(
            @PathVariable
            Long id
    ) {
        doctorService.delete(id);

        return ApiResponse.builder()
                .success(true)
                .message("Doctor deleted successfully!")
                .data(null)
                .build();
    }
}
