package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionCreateServiceRequest;

@Builder
public record MissionCreateRequest(
    
    @NotBlank(message = "미션 설명은 필수입니다.")
    String description
    
) {
    
    public MissionCreateServiceRequest toServiceRequest(Long memberId) {
        return MissionCreateServiceRequest.builder()
            .memberId(memberId)
            .missionDescription(description)
            .build();
    }
    
}
