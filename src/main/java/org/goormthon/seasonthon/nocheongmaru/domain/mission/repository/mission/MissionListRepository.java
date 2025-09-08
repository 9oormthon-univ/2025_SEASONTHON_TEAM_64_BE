package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;

import java.util.List;

public interface MissionListRepository {
    
    List<MissionResponse> getMissionsByMember(Long memberId);
    
}
