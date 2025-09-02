package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MissionNotFoundException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MissionRepository {
    
    private final MissionJpaRepository missionJpaRepository;

    public Mission findById(Long missionId) {
        return missionJpaRepository.findById(missionId)
            .orElseThrow(MissionNotFoundException::new);
    }
    
}
