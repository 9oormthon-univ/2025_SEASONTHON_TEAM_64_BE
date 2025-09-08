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
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionModifyServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class MissionServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionService missionService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @MockitoBean
    private MissionReader missionReader;
    
    @MockitoBean
    private MissionEditor missionEditor;
    
    @MockitoBean
    private MissionGenerator missionGenerator;
    
    @AfterEach
    void tearDown() {
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("관리자가 미션을 생성한다.")
    @Test
    void generate() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        var request = MissionCreateServiceRequest.builder()
            .memberId(member.getId())
            .missionDescription("missionDescription")
            .build();
        
        given(missionGenerator.generate(anyString(), any()))
            .willReturn(1L);
        
        // when
        Long missionId = missionService.generate(request);
        
        // then
        assertThat(missionId).isEqualTo(1L);
    }
    
    @DisplayName("관리자가 미션을 수정한다.")
    @Test
    void modifyMission() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "missionDescription");
        missionRepository.save(mission);
        
        Long missionId = mission.getId();
        var request = MissionModifyServiceRequest.builder()
            .memberId(member.getId())
            .missionId(missionId)
            .missionDescription("updatedMissionDescription")
            .build();
        
        // when
        missionService.modify(request);
        
        // then
        verify(missionEditor).modifyMission(any(), any(), anyString());
    }
    
    @DisplayName("관리자가 미션을 삭제한다.")
    @Test
    void deleteMission() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "missionDescription");
        missionRepository.save(mission);
        
        Long missionId = mission.getId();
        
        // when
        missionService.delete(member.getId(), missionId);
        
        // then
        verify(missionEditor).deleteMission(any(), any());
    }
    
    @DisplayName("관리자의 미션 목록을 조회한다.")
    @Test
    void getMissionsByMember() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission m1 = createMission(member, "mission1");
        Mission m2 = createMission(member, "mission2");
        Mission m3 = createMission(member, "mission3");
        missionRepository.saveAll(List.of(m1, m2, m3));
        
        given(missionReader.getMissionsByMember(any()))
            .willReturn(List.of(
                createMissionResponse(m1),
                createMissionResponse(m2),
                createMissionResponse(m3)
            ));
        
        // when
        List<MissionResponse> missions = missionService.getMissionsByMember(member.getId());
        
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
    
    @DisplayName("관리자의 미션 상세를 조회한다.")
    @Test
    void getMissionByMember() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "mission1");
        missionRepository.save(mission);
        
        given(missionReader.getMissionByMember(any()))
            .willReturn(createMissionResponse(mission));
        
        // when
        MissionResponse missionResponse = missionService.getMissionByMember(mission.getId());
        
        // then
        assertThat(missionResponse)
            .extracting("id", "description")
            .containsExactly(mission.getId(), "mission1");
    }
    
    @DisplayName("회원에게 할당된 미션을 조회한다.")
    @Test
    void getAllocatedMission() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, "mission1");
        missionRepository.save(mission);
        
        MemberMission memberMission = createMemberMission(member, mission);
        memberMissionRepository.save(memberMission);
        
        given(missionReader.getAllocatedMission(any()))
            .willReturn(createMissionResponse(mission));
        
        // when
        MissionResponse missionResponse = missionService.getAllocatedMission(member.getId());
        
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
    
    private MissionResponse createMissionResponse(Mission mission) {
        return MissionResponse.builder()
            .id(mission.getId())
            .description(mission.getDescription())
            .build();
    }
    
    private MemberMission createMemberMission(Member member, Mission mission) {
        return MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(java.time.LocalDate.now())
            .status(Status.ASSIGNED)
            .build();
    }
    
}