package org.goormthon.seasonthon.nocheongmaru.global.exception.common;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

public record ErrorResponse(
    int status,
    String message,
    Map<String, String> validationErrors
) {
    
    @Builder
    public ErrorResponse(int status, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }
    
    public void addValidationError(String field, String errorMessage) {
        this.validationErrors.put(field, errorMessage);
    }
    
    private Map<String, String> addValidationErrors(Map<String, String> errors) {
        if (errors != null) {
            return errors;
        }
        return new HashMap<>();
    }
    
}
