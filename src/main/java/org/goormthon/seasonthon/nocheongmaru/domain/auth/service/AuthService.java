package org.goormthon.seasonthon.nocheongmaru.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.dto.request.ReissueTokenServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.InvalidRefreshTokenException;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider.TokenProvider;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    
    @Transactional
    public TokenResponse reissueToken(ReissueTokenServiceRequest serviceRequest) {
        String accessToken = serviceRequest.accessToken();
        String refreshToken = serviceRequest.refreshToken();
        Long memberId = tokenProvider.getSubject(accessToken);
    
        validateRefreshToken(memberId, refreshToken);
        
        Member member = memberRepository.findById(memberId);
        TokenResponse tokenResponse = tokenProvider.issueToken(memberId, member.getRole());
        member.updateRefreshToken(tokenResponse.refreshToken());
        
        return tokenResponse;
    }
    
    private void validateRefreshToken(Long memberId, String refreshToken) {
        if (!memberRepository.existsByIdAndRefreshToken(memberId, refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
    }
    
}
