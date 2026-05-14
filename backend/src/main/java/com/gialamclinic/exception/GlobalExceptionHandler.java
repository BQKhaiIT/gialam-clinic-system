package com.gialamclinic.exception;

import com.gialamclinic.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            BadRequestException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleBadRequest(
            BadRequestException ex
    ){

        return ResponseEntity
                .badRequest()
                .body(

                        new ApiResponse<>(

                                false,

                                ex.getMessage(),

                                null

                        )
                );

    }

}