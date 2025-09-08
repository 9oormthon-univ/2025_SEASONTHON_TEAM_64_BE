package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MissionReader {
    
    private final MissionRepository missionRepository;
    
    @Transactional(readOnly = true)
    public List<MissionResponse> getMissionsByMember(Long memberId) {
        return missionRepository.getMissionsByMember(memberId);
    }
    
}
