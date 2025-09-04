package org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response;

import lombok.Builder;

@Builder
public record MemberDetailResponse(
    
    Long memberId,
    String nickname,
    String profileImageUrl,
    String role
    
) {
}
