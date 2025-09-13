package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MissionAssignerTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionAssigner missionAssigner;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @AfterEach
    void tearDown() {
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("모든 회원에게 오늘의 미션을 1개씩 배정한다.")
    @Test
    void assignDailyMission() {
        // given
        Member owner = createAdmin("owner@email");
        memberRepository.save(owner);
        
        List<Member> members = List.of(
            createMember("u1@email"),
            createMember("u2@email"),
            createMember("u3@email")
        );
        memberRepository.saveAll(members);
        
        List<Mission> missions = List.of(
            createMission(owner, "mission-1"),
            createMission(owner, "mission-2"),
            createMission(owner, "mission-3")
        );
        missionRepository.saveAll(missions);
        
        // when
        missionAssigner.assignDailyMission();
        
        // then
        LocalDate today = LocalDate.now();
        List<MemberMission> todayMissions = memberMissionRepository.findAllByForDate(today);
        assertThat(todayMissions).hasSize(members.size());
    }
    
    @DisplayName("일부 회원은 과거에 모든 미션을 이미 받아서 배정되지 않더라도 가능한 회원은 배정한다.")
    @Test
    void assignDailyMission_WhenSomeMembersHaveNoNewMissions() {
        // given
        Member owner = createAdmin("owner@email");
        memberRepository.save(owner);
        
        Mission mission = createMission(owner, "only-mission");
        missionRepository.saveAll(List.of(mission));
        
        Member m1 = createMember("a@email");
        Member m2 = createMember("b@email");
        memberRepository.saveAll(List.of(m1, m2));
        
        MemberMission history = createMemberMission(m1, mission, LocalDate.now().minusDays(1));
        memberMissionRepository.save(history);
        
        // when
        missionAssigner.assignDailyMission();
        
        // then
        LocalDate today = LocalDate.now();
        List<MemberMission> todayMission = memberMissionRepository.findAllByForDate(today);
        assertThat(todayMission)
            .extracting(memberMission -> memberMission.getMember().getId(), memberMission -> memberMission.getMission().getId())
            .containsExactly(
                tuple(m2.getId(), mission.getId())
            );
    }
    
    @DisplayName("오늘 이미 배정된 회원은 건너뛰고 나머지만 배정한다.")
    @Test
    void assignDailyMission_SkipAlreadyAssignedToday() {
        // given
        Member owner = createAdmin("owner@email");
        memberRepository.save(owner);
        
        Member m1 = createMember("m1@email");
        Member m2 = createMember("m2@email");
        memberRepository.saveAll(List.of(m1, m2));
        
        Mission mission = createMission(owner, "mission");
        missionRepository.saveAll(List.of(mission));
        
        MemberMission todayForMission = createMemberMission(m1, mission, LocalDate.now());
        memberMissionRepository.save(todayForMission);
        
        // when
        missionAssigner.assignDailyMission();
        
        // then
        LocalDate today = LocalDate.now();
        List<MemberMission> todayMission = memberMissionRepository.findAllByForDate(today);
        assertThat(todayMission).hasSize(2);
    }
    
    private Member createMember(String email) {
        return Member.builder()
            .email(email)
            .nickname("nickname-" + email)
            .profileImageURL("profileImageUrl")
            .role(Role.ROLE_USER)
            .build();
    }
    
    private Member createAdmin(String email) {
        return Member.builder()
            .email(email)
            .nickname("nickname-" + email)
            .profileImageURL("profileImageUrl")
            .role(Role.ROLE_ADMIN)
            .build();
    }
    
    private Mission createMission(Member owner, String description) {
        return Mission.builder()
            .description(description)
            .member(owner)
            .build();
    }
    
    private MemberMission createMemberMission(Member member, Mission mission, LocalDate forDate) {
        return MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(forDate)
            .build();
    }
    
}
