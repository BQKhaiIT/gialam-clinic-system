package com.gialamclinic.exception;

import com.gialamclinic.response.ApiResponse;
import com.gialamclinic.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErrorResponse>
    handleValidation(
            MethodArgumentNotValidException ex
    ){

        Map<String,String> errors=
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error->

                        errors.put(

                                error.getField(),

                                error.getDefaultMessage()

                        )

                );

        return ResponseEntity
                .badRequest()
                .body(

                        new ErrorResponse(

                                "Validation failed",
                                false,
                                errors

                        )

                );

    }

}