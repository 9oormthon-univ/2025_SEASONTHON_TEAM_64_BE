package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.request;

import lombok.Builder;

@Builder
public record FortuneCreateServiceRequest(
    
    Long memberId,
    String description
    
) {
}
