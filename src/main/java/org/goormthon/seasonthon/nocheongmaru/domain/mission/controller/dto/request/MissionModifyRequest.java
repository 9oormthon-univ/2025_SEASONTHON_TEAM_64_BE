package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionModifyServiceRequest;

@Builder
public record MissionModifyRequest(
    
    @NotNull(message = "미션 ID는 필수입니다.")
    Long missionId,
    
    @NotBlank(message = "미션 설명은 필수입니다.")
    String description
    
) {
    
    public MissionModifyServiceRequest toServiceRequest(Long memberId) {
        return MissionModifyServiceRequest.builder()
            .memberId(memberId)
            .missionId(missionId)
            .missionDescription(description)
            .build();
    }
    
}
