package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MissionModifier {
    
    private final MissionRepository missionRepository;
    
    @Transactional
    public void modifyMission(Long missionId, String missionDescription) {
        Mission mission = missionRepository.findById(missionId);
        mission.modifyTitle(missionDescription);
    }
    
    @Transactional
    public void deleteMission(Long missionId) {
        missionRepository.deleteById(missionId);
    }
    
}
