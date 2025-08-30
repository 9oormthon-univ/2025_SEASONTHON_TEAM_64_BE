package org.goormthon.seasonthon.nocheongmaru.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    
    private static final String EXCEPTION_LOG =
        "[Exception] Status Code: {}, Error Message: {}";
    private static final String VALIDATION_LOG =
        "[Validation] Field: %s, Message: %s";
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseException(BaseException e) {
        int statusCode = e.getErrorCode().getStatus();
        log.error(EXCEPTION_LOG, statusCode, e.getMessage(), e.getCause());
        
        ErrorResponse response = generateErrorResponse(e);
        
        return ResponseEntity
            .status(statusCode)
            .body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        int statusCode = INTERNAL_SERVER_ERROR.value();
        log.error(EXCEPTION_LOG, statusCode, e.getMessage(), e.getCause());
        
        ErrorResponse response = ErrorResponse.builder()
            .status(statusCode)
            .message(e.getMessage())
            .build();
        
        return ResponseEntity
            .status(statusCode)
            .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        int statusCode = e.getStatusCode().value();
        
        Map<String, String> validation = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        ErrorResponse response = ErrorResponse.builder()
            .status(statusCode)
            .message("잘못된 요청입니다.")
            .validationErrors(validation)
            .build();
        
        return ResponseEntity
            .status(statusCode)
            .body(response);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        int statusCode = e.getStatusCode().value();
        String errorMessage = String.format(VALIDATION_LOG, e.getParameterName(), e.getMessage());
        log.error(errorMessage);
        
        ErrorResponse response = ErrorResponse.builder()
            .status(statusCode)
            .message(e.getParameterName() + "가 누락되었습니다.")
            .build();
        
        return ResponseEntity
            .status(statusCode)
            .body(response);
    }
    
    private ErrorResponse generateErrorResponse(BaseException e) {
        return ErrorResponse.builder()
            .status(e.getErrorCode().getStatus())
            .message(e.getMessage())
            .validationErrors(e.getValidationErrors())
            .build();
    }
    
}
