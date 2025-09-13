package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.request.FortuneCreateServiceRequest;

@Builder
public record FortuneCreateRequest(
    
    @NotBlank(message = "포춘쿠키 설명은 필수입니다.")
    String description
    
) {
    
    public FortuneCreateServiceRequest toServiceRequest(Long memberId) {
        return FortuneCreateServiceRequest.builder()
            .memberId(memberId)
            .description(description)
            .build();
    }
}
