package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.mission.IsNotMissionOwnerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MissionModifier {
    
    private final MissionRepository missionRepository;
    
    @Transactional
    public void modifyMission(Long memberId, Long missionId, String missionDescription) {
        validateMissionOwnership(memberId, missionId);
        Mission mission = missionRepository.findById(missionId);
        mission.modifyTitle(missionDescription);
    }
    
    @Transactional
    public void deleteMission(Long memberId, Long missionId) {
        validateMissionOwnership(memberId, missionId);
        missionRepository.deleteById(missionId);
    }
    
    private void validateMissionOwnership(Long memberId, Long missionId) {
        if (!missionRepository.existsByIdAndMemberId(missionId, memberId)) {
            throw new IsNotMissionOwnerException();
        }
    }
    
}
