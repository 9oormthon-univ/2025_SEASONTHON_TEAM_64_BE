package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.TodayMissionNotFoundException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemberMissionRepository {
    
    private final MemberMissionJpaRepository memberMissionJpaRepository;
    
    public boolean existsByMemberIdAndForDate(Long memberId, LocalDate forDate) {
        return memberMissionJpaRepository.existsByMemberIdAndForDate(memberId, forDate);
    }
    
    public boolean existsByMemberIdAndMissionId(Long memberId, Long missionId) {
        return memberMissionJpaRepository.existsByMemberIdAndMissionId(memberId, missionId);
    }
    
    public MemberMission findByMemberIdAndForDate(Long memberId, LocalDate forDate) {
        return memberMissionJpaRepository.findByMemberIdAndForDate(memberId, forDate)
            .orElseThrow(TodayMissionNotFoundException::new);
    }
    
    public MemberMission save(MemberMission memberMission) {
        return memberMissionJpaRepository.save(memberMission);
    }
    
    public void deleteAllInBatch() {
        memberMissionJpaRepository.deleteAllInBatch();
    }
    
}
