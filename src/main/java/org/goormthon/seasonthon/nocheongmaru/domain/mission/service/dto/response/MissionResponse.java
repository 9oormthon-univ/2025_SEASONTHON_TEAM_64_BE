package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;

@Builder
public record MissionResponse(
    
    Long id,
    String description

) {
    
    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
            .id(mission.getId())
            .description(mission.getDescription())
            .build();
    }
    
}
