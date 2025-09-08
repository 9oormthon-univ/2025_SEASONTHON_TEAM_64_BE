package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MissionReader {
    
    private final MemberMissionRepository memberMissionRepository;
    private final MissionRepository missionRepository;
    
    @Transactional(readOnly = true)
    public List<MissionResponse> getMissionsByMember(Long memberId) {
        return missionRepository.getMissionsByMember(memberId);
    }
    
    @Transactional(readOnly = true)
    public MissionResponse getMissionByMember(Long missionId) {
        return missionRepository.getMissionByMember(missionId);
    }
    
    @Transactional(readOnly = true)
    public MissionResponse getAllocatedMission(Long memberId) {
        LocalDate today = LocalDate.now();
        MemberMission memberMission = memberMissionRepository.findByMemberIdAndForDate(memberId, today);
        
        return MissionResponse.builder()
            .id(memberMission.getMission().getId())
            .description(memberMission.getMission().getDescription())
            .build();
    }
    
}
