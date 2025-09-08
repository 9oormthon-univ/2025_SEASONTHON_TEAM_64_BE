package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MissionGenerator {
    
    private final MissionRepository missionRepository;
    
    @Transactional
    public Long generate(String missionDescription, Member member) {
        Mission mission = Mission.builder()
            .description(missionDescription)
            .member(member)
            .build();
        missionRepository.save(mission);
        
        return mission.getId();
    }
    
}
