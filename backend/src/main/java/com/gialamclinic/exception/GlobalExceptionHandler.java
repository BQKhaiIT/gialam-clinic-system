package com.gialamclinic.exception;

import com.gialamclinic.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<Object>> buildResponse(
            HttpStatus status,
            String message,
            Object data
    ) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(message)
                .data(data)
                .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }

    // NOT FOUND

    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleNotFound(
            ResourceNotFoundException ex
    ){
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
        );
    }


    // BAD REQUEST

    @ExceptionHandler(
            BadRequestException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleBadRequest(
            BadRequestException ex
    ){
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }


    // CONFLICT

    @ExceptionHandler(
            ConflictException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleConflict(
            ConflictException ex
    ){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(

                        ApiResponse.builder()
                                .success(false)
                                .message(
                                        ex.getMessage()
                                )
                                .build()

                );

    }


    // VALIDATION ERROR

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleValidation(
            MethodArgumentNotValidException ex
    ){

        Map<String,String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->

                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )

                );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
    }


    // CONSTRAINT VIOLATION

    @ExceptionHandler(
            ConstraintViolationException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleConstraintViolation(
            ConstraintViolationException ex
    ){
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(violation ->
                        errors.put(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
    }


    // INVALID BODY

    @ExceptionHandler(
            HttpMessageNotReadableException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleUnreadableMessage(
            HttpMessageNotReadableException ex
    ){
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is invalid or malformed",
                null
        );
    }


    // DATA INTEGRITY

    @ExceptionHandler(
            DataIntegrityViolationException.class
    )

    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(

            DataIntegrityViolationException ex

    ){

        ex.printStackTrace();

        return ResponseEntity
                .status(409)
                .body(

                        ApiResponse.builder()

                                .success(false)

                                .message(

                                        ex.getMostSpecificCause()
                                                .getMessage()

                                )

                                .data(null)

                                .build()

                );

    }


    // GENERAL EXCEPTION

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleGeneral(
            Exception ex
    ){

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null
        );
    }



}
