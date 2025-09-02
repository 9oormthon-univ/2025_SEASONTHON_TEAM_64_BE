package org.goormthon.seasonthon.nocheongmaru.domain.auth.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.dto.request.ReissueTokenServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.InvalidRefreshTokenException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role.ROLE_USER;
import static org.mockito.BDDMockito.given;

class AuthServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @MockitoBean
    private TokenProvider tokenProvider;
    
    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("리프레시 토큰이 유효한 경우, 토큰을 재발급한다.")
    @Test
    void reissueToken() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        given(tokenProvider.getSubject("access-token"))
            .willReturn(member.getId());
        
        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken("new-access-token")
            .refreshToken("new-refresh-token")
            .build();
        given(tokenProvider.issueToken(member.getId(), member.getRole()))
            .willReturn(tokenResponse);
        
        ReissueTokenServiceRequest request = ReissueTokenServiceRequest.builder()
            .accessToken("access-token")
            .refreshToken("valid-refresh-token")
            .build();
        
        // when
        TokenResponse result = authService.reissueToken(request);
        
        // then
        assertThat(result.accessToken()).isEqualTo("new-access-token");
        assertThat(result.refreshToken()).isEqualTo("new-refresh-token");
    }
    
    @DisplayName("리프레시 토큰이 유효하지 않은 경우, 예외를 발생시킨다.")
    @Test
    void reissueToken_WithInvalidRefreshToken() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        given(tokenProvider.getSubject("access-token"))
            .willReturn(member.getId());
        
        ReissueTokenServiceRequest request = ReissueTokenServiceRequest.builder()
            .accessToken("access-token")
            .refreshToken("invalid-refresh-token")
            .build();
        
        // when & then
        assertThatThrownBy(() -> authService.reissueToken(request))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessageContaining(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }
    
    private Member createMember() {
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("test")
            .profileImageURL("http://example.com/profile.jpg")
            .role(ROLE_USER)
            .build();
        
        member.updateRefreshToken("valid-refresh-token");
        return member;
    }
    
}