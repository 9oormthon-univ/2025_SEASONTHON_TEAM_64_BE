package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access denied: {}", accessDeniedException.getMessage());
        sendErrorResponse(response);
    }
    
    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(SC_FORBIDDEN)
            .message(ErrorCode.FORBIDDEN.getMessage())
            .build();
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        
        response.setStatus(SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonResponse);
    }
    
}
