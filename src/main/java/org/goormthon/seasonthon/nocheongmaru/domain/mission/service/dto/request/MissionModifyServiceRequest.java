package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request;

import lombok.Builder;

@Builder
public record MissionModifyServiceRequest(
    
    Long memberId,
    Long missionId,
    String missionDescription
    
) {
}
