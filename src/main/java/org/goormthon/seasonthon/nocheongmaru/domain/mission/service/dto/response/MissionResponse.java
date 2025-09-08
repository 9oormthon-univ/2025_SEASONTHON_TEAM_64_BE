package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response;

import lombok.Builder;

@Builder
public record MissionResponse(
    
    Long id,
    String description

) {
}
