package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;

@Builder
public record MissionResponse(
    
    Long id,
    String title

) {
    
    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
            .id(mission.getId())
            .title(mission.getTitle())
            .build();
    }
    
}
