package org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response;

import lombok.Builder;

@Builder
public record MemberDetailResponse(
    
    Long memberId,
    
    String nickname,
    
    String email,
    
    String profileImageUrl,
    
    String role,
    
    String mode
    
) {
}
