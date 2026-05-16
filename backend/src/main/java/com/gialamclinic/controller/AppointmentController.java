package com.gialamclinic.controller;

import com.gialamclinic.dto.request.AppointmentRequest;
import com.gialamclinic.dto.response.ApiResponse;
import com.gialamclinic.dto.response.AppointmentResponse;
import com.gialamclinic.dto.request.AppointmentStatusRequest;
import com.gialamclinic.entity.Appointment;
import com.gialamclinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@CrossOrigin
public class AppointmentController {

    private final AppointmentService service;


    // CREATE

    @PostMapping
    public ApiResponse<AppointmentResponse> create(

            @RequestBody
            @Valid
            AppointmentRequest request
    ){

        AppointmentResponse response =
                service.create(request);

        return ApiResponse
                .<AppointmentResponse>builder()
                .success(true)
                .message("Appointment created successfully!")
                .data(response)
                .build();

    }


    // GET ALL

    @GetMapping
    public ApiResponse<Page<AppointmentResponse>> getAll(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ){

        Page<AppointmentResponse> response =
                service.getAll(
                        page,
                        size
                );

        return ApiResponse
                .<Page<AppointmentResponse>>builder()
                .success(true)
                .message("Appointment list fetched successfully!")
                .data(response)
                .build();

    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getById(

            @PathVariable
            Long id
    ){

        AppointmentResponse response =
                service.getById(id);

        return ApiResponse
                .<AppointmentResponse>builder()
                .success(true)
                .message("Appointment fetched successfully!")
                .data(response)
                .build();

    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> update(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            AppointmentRequest request
    ){
        AppointmentResponse response =
                service.update(id, request);
        return ApiResponse
                .<AppointmentResponse>builder()
                .success(true)
                .message("Appointment updated successfully!")
                .data(response)
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiResponse<AppointmentResponse> updateStatus(

            @PathVariable
            Long id,

            @RequestBody
            @Valid
            AppointmentStatusRequest request
    ){

        AppointmentResponse response =

                service.updateStatus(
                        id,
                        request
                );

        return ApiResponse
                .<AppointmentResponse>builder()
                .success(true)
                .message("Appointment status updated successfully!")
                .data(response)
                .build();

    }
    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(

            @PathVariable
            Long id
    ){

        service.delete(id);

        return ApiResponse.builder()
                .success(true)
                .message("Appointment deleted successfully!")
                .data(null)
                .build();

    }

}