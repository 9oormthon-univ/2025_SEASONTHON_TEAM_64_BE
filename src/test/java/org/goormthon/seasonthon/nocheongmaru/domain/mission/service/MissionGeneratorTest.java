package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MissionGeneratorTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionGenerator missionGenerator;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @AfterEach
    void tearDown() {
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("관리자가 미션을 생성한다.")
    @Test
    void generate() {
        // given
        String missionDescription = "missionDescription";
        
        Member member = createMember();
        memberRepository.save(member);
        
        // when
        Long missionId = missionGenerator.generate(missionDescription, member);
        
        // then
        Mission mission = missionRepository.findById(missionId);
        assertThat(mission.getId()).isEqualTo(missionId);
    }
    
    private Member createMember() {
        return Member.builder()
            .email("email")
            .nickname("nickname")
            .profileImageURL("profileImageUrl")
            .role(Role.ROLE_ADMIN)
            .build();
    }
    
}