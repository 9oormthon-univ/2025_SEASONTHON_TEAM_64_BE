package org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.request;

import lombok.Builder;

@Builder
public record MemberDeviceTokenServiceRequest(
    
    String deviceToken,
    Long memberId
    
) {
}
