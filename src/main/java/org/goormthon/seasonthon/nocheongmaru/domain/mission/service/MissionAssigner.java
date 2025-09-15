package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.MissionAssignedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionAssigner {
    
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public void assignDailyMission() {
        LocalDate today = LocalDate.now();
        Set<Long> alreadyAssignedToday = getAlreadyAssignedMemberIds(today);
        List<Member> members = getAllMembers().stream()
            .filter(m -> !alreadyAssignedToday.contains(m.getId()))
            .filter(m -> m.getRole() != Role.ROLE_ADMIN)
            .toList();
        if (members.isEmpty()) {
            log.info("[MissionAssign] 오늘 배정 대상 회원이 없습니다.");
            return;
        }
        
        List<Long> allMissionIds = getAllMissionIds();
        if (allMissionIds.isEmpty()) {
            log.warn("[MissionAssign] 등록된 미션이 없어 배정을 수행하지 않습니다.");
            return;
        }
        
        List<Object[]> params = buildAssignments(members, allMissionIds, today);
        if (params.isEmpty()) {
            log.info("[MissionAssign] 배정 가능한 회원이 없습니다. 작업을 종료합니다.");
            return;
        }
        
        batchInsertMemberMissions(params);
        
        List<Long> assignedMemberIds = resolveMemberIds(params);
        eventPublisher.publishEvent(new MissionAssignedEvent(assignedMemberIds));
        log.info("[MissionAssign] 총 {}명 중 {}건 배정 완료 (부분 배정 가능)", members.size(), params.size());
    }
    
    private Set<Long> getAlreadyAssignedMemberIds(LocalDate date) {
        return memberMissionRepository.findAllByForDate(date).stream()
            .map(mm -> mm.getMember().getId())
            .collect(Collectors.toSet());
    }
    
    private List<Object[]> buildAssignments(List<Member> members, List<Long> allMissionIds, LocalDate forDate) {
        List<Long> sortedMissionIds = new ArrayList<>(allMissionIds);
        Collections.sort(sortedMissionIds);
        
        List<Object[]> results = new ArrayList<>();
        int memberIndex = 0;
        for (Member member : members) {
            List<Long> prev = getPreviouslyAssignedMissionIds(member.getId());
            Long missionId = pickMission(sortedMissionIds, prev, memberIndex);
            if (missionId != null) {
                results.add(new Object[]{member.getId(), missionId, java.sql.Date.valueOf(forDate)});
            }
            memberIndex++;
        }
        return results;
    }
    
    private Long pickMission(List<Long> allMissions, List<Long> prevAssigned, int offset) {
        if (allMissions.isEmpty()) return null;
        Set<Long> prevSet = new HashSet<>(prevAssigned);
        int n = allMissions.size();
        
        for (int i = 0; i < n; i++) {
            Long candidate = allMissions.get((offset + i) % n);
            if (!prevSet.contains(candidate)) {
                return candidate;
            }
        }
        
        return null;
    }
    
    private void batchInsertMemberMissions(List<Object[]> params) {
        String sql = "INSERT INTO member_missions (member_id, mission_id, for_date) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, params);
    }
    
    private List<Long> resolveMemberIds(List<Object[]> params) {
        return params.stream()
            .map(p -> (Long) p[0])
            .distinct()
            .toList();
    }
    
    private List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    private List<Long> getAllMissionIds() {
        return missionRepository.findAllMissionIds();
    }
    
    private List<Long> getPreviouslyAssignedMissionIds(Long memberId) {
        return memberMissionRepository.findAllMissionIdsByMemberId(memberId);
    }
    
}
