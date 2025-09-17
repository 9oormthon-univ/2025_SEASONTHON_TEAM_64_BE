package org.goormthon.seasonthon.nocheongmaru.global.security.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider.TokenProvider;
import org.goormthon.seasonthon.nocheongmaru.global.security.oauth.CustomOAuth2Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    
    @Override
    @Transactional
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = oAuth2Member.getMemberId();
        
        Member member = memberRepository.findById(memberId);
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, member.getRole());
        saveRefreshToken(member, tokenResponse.refreshToken());
        
        String redirectUrl = buildRedirectUrl(tokenResponse);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
    
    private String buildRedirectUrl(TokenResponse tokenResponse) {
        return UriComponentsBuilder.fromUriString("https://www.nocheongmaru.site/oauth2/redirect")
            .queryParam("accessToken", tokenResponse.accessToken())
            .queryParam("refreshToken", tokenResponse.refreshToken())
            .build()
            .toUriString();
    }
    
    private void saveRefreshToken(Member member, String refreshToken) {
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }
    
}