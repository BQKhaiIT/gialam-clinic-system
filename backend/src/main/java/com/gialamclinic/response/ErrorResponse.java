package com.gialamclinic.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private boolean success;
    private Map<String, String> errors;
}
