package org.goormthon.seasonthon.nocheongmaru.global.exception.common;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException {
    
    private final Map<String, String> validationErrors;
    private final ErrorCode errorCode;
    
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.validationErrors = new HashMap<>();
    }
    
    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.validationErrors = new HashMap<>();
    }
    
    public void addValidationError(String field, String message) {
        validationErrors.put(field, message);
    }
    
    public Map<String, String> getValidationErrors() {
        return Collections.unmodifiableMap(validationErrors);
    }
    
}
