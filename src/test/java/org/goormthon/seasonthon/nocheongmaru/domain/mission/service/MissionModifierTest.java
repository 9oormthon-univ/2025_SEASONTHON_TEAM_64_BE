package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.mission.IsNotMissionOwnerException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.mission.MissionNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionModifierTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionModifier missionModifier;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @AfterEach
    void tearDown() {
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("관리자가 미션을 수정한다.")
    @Test
    void modifyMission() {
        // given
        String missionDescription = "missionDescription";
        String updatedMissionDescription = "updatedMissionDescription";
        
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, missionDescription);
        missionRepository.save(mission);
        
        Long missionId = mission.getId();
        
        // when
        missionModifier.modifyMission(member.getId(), mission.getId(), updatedMissionDescription);
        
        // then
        Mission updatedMission = missionRepository.findById(missionId);
        assertThat(updatedMission.getDescription()).isEqualTo(updatedMissionDescription);
    }
    
    @DisplayName("자신이 생성하지 않은 미션은 수정할 수 없다.")
    @Test
    void modifyMission_NotOwner() {
        // given
        String missionDescription = "missionDescription";
        
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, missionDescription);
        missionRepository.save(mission);
        
        // expected
        assertThatThrownBy(() -> missionModifier.modifyMission(member.getId() + 1, mission.getId(), "updatedMissionDescription"))
            .isInstanceOf(IsNotMissionOwnerException.class)
            .hasMessage("미션의 작성자가 아닙니다.");
    }
    
    @DisplayName("관리자가 미션을 삭제한다.")
    @Test
    void deleteMission() {
        // given
        String missionDescription = "missionDescription";
        
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, missionDescription);
        missionRepository.save(mission);
        
        Long missionId = mission.getId();
        
        // when
        missionModifier.deleteMission(member.getId(), missionId);
        
        // then
        assertThatThrownBy(() -> missionRepository.findById(missionId))
            .isInstanceOf(MissionNotFoundException.class)
            .hasMessage("미션을 찾을 수 없습니다.");
    }
    
    @DisplayName("자신이 생성하지 않은 미션은 삭제할 수 없다.")
    @Test
    void deleteMission_NotOwner() throws Exception {
        // given
        String missionDescription = "missionDescription";
        
        Member member = createMember();
        memberRepository.save(member);
        
        Mission mission = createMission(member, missionDescription);
        missionRepository.save(mission);
        
        Long missionId = mission.getId();
        
        // expected
        assertThatThrownBy(() -> missionModifier.deleteMission(member.getId() + 1, mission.getId()))
            .isInstanceOf(IsNotMissionOwnerException.class)
            .hasMessage("미션의 작성자가 아닙니다.");
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