package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

class TokenProviderTest extends IntegrationTestSupport {
    
    private TokenProvider tokenProvider;
    
    @Value("${jwt.secret}")
    private String secret;
    
    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(secret);
    }
    
    @DisplayName("Access Token과 Refresh Token을 생성한다.")
    @Test
    void issueToken() {
        // Given
        Long memberId  = 1L;
        Role role = Role.ROLE_USER;
        
        // When
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, role);
        
        // Then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isNotBlank();
        assertThat(tokenResponse.refreshToken()).isNotBlank();
    }
    
    @DisplayName("Access Token의 Subject를 반환한다.")
    @Test
    void getSubject() {
        // Given
        Long memberId  = 1L;
        Role role = Role.ROLE_USER;
        
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, role);
        
        // When
        Long subject = tokenProvider.getSubject(tokenResponse.accessToken());
        
        // Then
        assertThat(subject).isEqualTo(memberId);
    }
    
    @DisplayName("Authentication 객체를 반환한다.")
    @Test
    void getAuthentication() {
        // Given
        Long memberId  = 1L;
        Role role = Role.ROLE_USER;
        
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, role);
        
        // When
        Authentication authentication = tokenProvider.getAuthentication(tokenResponse.accessToken());
        
        // Then
        assertThat(authentication.getName()).isEqualTo(memberId.toString());
        assertThat(authentication.getAuthorities()).isNotEmpty();
        
    }
    
    @DisplayName("유효한 토큰인지 검증한다.")
    @Test
    void validateToken() {
        // Given
        Long memberId  = 1L;
        Role role = Role.ROLE_USER;
        
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, role);
        String targetToken = tokenResponse.accessToken();
        
        // When
        boolean isValid = tokenProvider.validateToken(targetToken);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @DisplayName("유효하지 않은 토큰은 검증에 실패한다.")
    @Test
    void validateInvalidToken() {
        // Given
        String targetToken = "Dummy Token";
        
        // When
        boolean isValid = tokenProvider.validateToken(targetToken);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
}