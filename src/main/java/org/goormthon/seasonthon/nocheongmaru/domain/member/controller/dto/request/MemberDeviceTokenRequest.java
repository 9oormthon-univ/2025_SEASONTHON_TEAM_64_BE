package org.goormthon.seasonthon.nocheongmaru.domain.member.controller.dto.request;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.request.MemberDeviceTokenServiceRequest;

@Builder
public record MemberDeviceTokenRequest(
    
    String deviceToken
    
) {
    
    public MemberDeviceTokenServiceRequest toServiceRequest(Long memberId) {
        return MemberDeviceTokenServiceRequest.builder()
            .memberId(memberId)
            .deviceToken(deviceToken)
            .build();
    }
}
