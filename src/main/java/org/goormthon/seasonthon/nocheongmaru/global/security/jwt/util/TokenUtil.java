package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.util;

import org.springframework.util.StringUtils;

import java.util.Optional;

public record TokenUtil() {
    
    private static final String TOKEN_PREFIX = "Bearer ";
    
    public static Optional<String> extractTokenFromHeader(String header) {
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return Optional.of(header.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }
    
}
