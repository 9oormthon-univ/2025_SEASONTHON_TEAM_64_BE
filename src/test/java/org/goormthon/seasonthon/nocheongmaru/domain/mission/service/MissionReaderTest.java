package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Status;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class MissionReaderTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionReader missionReader;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @AfterEach
    void tearDown() {
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("관리자의 미션 목록을 조회한다.")
    @Test
    void getMissionsByMember() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        missionRepository.saveAll(List.of(
            createMission(member, "mission1"),
            createMission(member, "mission2"),
            createMission(member, "mission3")
        ));
        
        // when
        List<MissionResponse> missions = missionReader.getMissionsByMember(member.getId());
        
        // then
        assertThat(missions).hasSize(3);
        assertThat(missions)
            .extracting("id", "description")
            .containsExactlyInAnyOrder(
                tuple(missions.get(0).id(), "mission1"),
                tuple(missions.get(1).id(), "mission2"),
                tuple(missions.get(2).id(), "mission3")
            );
    }
    
    @DisplayName("관리자의 미션 목록을 상세 조회한다.")
    @Test
    void getMissionByMember() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "mission1");
        missionRepository.save(mission);
        
        // when
        MissionResponse missionResponse = missionReader.getMissionByMember(mission.getId());
        
        // then
        assertThat(missionResponse)
            .extracting("id", "description")
            .containsExactly(mission.getId(), "mission1");
    }
    
    @DisplayName("오늘의 미션을 조회한다.")
    @Test
    void getAllocatedMission() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "mission1");
        missionRepository.save(mission);
        
        MemberMission memberMission = MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(LocalDate.now())
            .status(Status.ASSIGNED)
            .build();
        memberMissionRepository.save(memberMission);
        
        // when
        MissionResponse missionResponse = missionReader.getAllocatedMission(member.getId());
        
        // then
        assertThat(missionResponse)
            .extracting("id", "description")
            .containsExactly(mission.getId(), "mission1");
    }
    
    private Member createMember() {
        return Member.builder()
            .email("email")
            .nickname("nickname")
            .profileImageURL("profileImageUrl")
            .role(Role.ROLE_ADMIN)
            .build();
    }
    
    private Mission createMission(Member member, String missionDescription) {
        return Mission.builder()
            .description(missionDescription)
            .member(member)
            .build();
    }
    
}