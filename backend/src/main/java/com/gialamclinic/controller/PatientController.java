package com.gialamclinic.controller;

import com.gialamclinic.dto.request.PatientRequest;
import com.gialamclinic.dto.response.ApiResponse;
import com.gialamclinic.dto.response.PatientResponse;
import com.gialamclinic.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@CrossOrigin
public class PatientController {

    private final PatientService service;

    public PatientController(
            PatientService service) {
        this.service = service;
    }

    //CREATE

    @PostMapping
    public ApiResponse<PatientResponse> create(
            @RequestBody
            @Valid
            PatientRequest request) {

        PatientResponse response = service.create(request);

        return ApiResponse
                .<PatientResponse>builder()
                .success(true)
                .message("Patient created successfully!")
                .data(response)
                .build();
    }


    //GET ALL

    @GetMapping
    public ApiResponse<Page<PatientResponse>> getAll(

            @RequestParam(
                    defaultValue = "0")
            int page,

            @RequestParam(
                    defaultValue = "10")
            int size) {
        Page<PatientResponse> response =
                service.getAll(page, size);
        return ApiResponse
                .<Page<PatientResponse>>builder()
                .success(true)
                .message("Patient list fetched successfully")
                .data(response)
                .build();
    }


    //SEARCH

    @GetMapping("/search")
    public ApiResponse<Page<PatientResponse>> search(

            @RequestParam
            String keyword,

            @RequestParam(
                    defaultValue = "0")
            int page,

            @RequestParam(
                    defaultValue = "10")
            int size) {

        Page<PatientResponse> response = service.search(
                keyword,
                page,
                size
        );
        return ApiResponse
                .<Page<PatientResponse>>builder()
                .success(true)
                .message("Patient searched successfully!")
                .data(response)
                .build();

    }


    //UPDATE

    @PutMapping("/{id}")
    public ApiResponse<PatientResponse> update(
            @PathVariable Long id,
            @RequestBody PatientRequest request) {

        PatientResponse response = service.update(
                id,
                request
        );
        return ApiResponse
                .<PatientResponse>builder()
                .success(true)
                .message("Patient updated successfully!")
                .data(response)
                .build();
    }


    //DELETE

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ApiResponse.builder()
                .success(true)
                .message("Patient deleted successfully!")
                .data(null)
                .build();
    }

}