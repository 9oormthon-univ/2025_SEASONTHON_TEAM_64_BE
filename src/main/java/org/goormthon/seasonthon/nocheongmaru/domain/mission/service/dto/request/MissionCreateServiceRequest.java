package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request;

import lombok.Builder;

@Builder
public record MissionCreateServiceRequest(
    
    Long memberId,
    String missionDescription
    
) {

}
