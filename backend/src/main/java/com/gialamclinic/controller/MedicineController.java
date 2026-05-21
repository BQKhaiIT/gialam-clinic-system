package com.gialamclinic.controller;
import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.*;
import com.gialamclinic.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
@CrossOrigin
public class MedicineController {
    private final MedicineService service;

    @PostMapping
    public ApiResponse<MedicineResponse> create (
            @RequestBody
            @Valid
            MedicineRequest request
    ) {
        return ApiResponse
                .<MedicineResponse>builder()
                .success(true)
                .message("Medicine created")
                .data(
                        service.create(request)
                )
                .build();

    }

    @GetMapping
    public ApiResponse<Page<MedicineResponse>> getAll(
            @RequestParam(
                    defaultValue = "0"
            )
            int page,
            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ){
        return ApiResponse
                .<Page<MedicineResponse>>builder()
                .success(true)
                .message("Medicine fetched")
                .data(
                        service.getAll(
                                page, size)
                )
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<MedicineResponse>> search(
            @RequestParam
            String keyword,
            @RequestParam(
                    defaultValue = "0"
            )
            int page,
            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ){
        return ApiResponse
                .<Page<MedicineResponse>>builder()
                .success(true)
                .message("Medicine searched")
                .data(
                        service.search(
                                keyword,
                                page,
                                size
                        )
                )
                .build();
    }
    @PutMapping("/{id}/stock")
    public ApiResponse<MedicineResponse>

    updateStock(

            @PathVariable
            Long id,

            @RequestBody
            UpdateStockRequest request

    ){

        return ApiResponse
                .<MedicineResponse>builder()

                .success(true)

                .message("Stock updated")

                .data(

                        service.updateStock(
                                id,
                                request
                        )

                )

                .build();

    }


    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(

            @PathVariable
            Long id

    ){

        service.delete(
                id
        );

        return ApiResponse.builder()

                .success(true)

                .message("Medicine deleted")

                .data(null)

                .build();

    }
}
