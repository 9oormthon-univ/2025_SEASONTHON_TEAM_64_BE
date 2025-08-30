package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider.TokenProvider;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.util.TokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    
    private static final String TOKEN_HEADER = "Authorization";
    
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            getAuthentication(request).ifPresent(authentication ->
                SecurityContextHolder.getContext().setAuthentication(authentication)
            );
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            sendErrorResponse(response);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        
        return TokenUtil.extractTokenFromHeader(token)
            .filter(tokenProvider::validateToken)
            .map(tokenProvider::getAuthentication);
    }
    
    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(SC_UNAUTHORIZED)
            .message(ErrorCode.INVALID_ACCESS_TOKEN.getMessage())
            .build();
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonResponse);
    }
    
}
