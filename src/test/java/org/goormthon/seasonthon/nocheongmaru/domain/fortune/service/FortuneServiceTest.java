package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.request.FortuneCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FortuneServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private FortuneService fortuneService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private FortuneRepository fortuneRepository;
    
    @AfterEach
    void tearDown() {
        fortuneRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("오늘의 포춘쿠리를 생성한다.")
    @Test
    void generateFortune() {
        // given
        Member member = createMember("user", "user@test.com");
        memberRepository.save(member);
        
        FortuneCreateServiceRequest request = FortuneCreateServiceRequest.builder()
            .memberId(member.getId())
            .description("행운이 찾아올 거예요!")
            .build();
        
        // when
        Long fortuneId = fortuneService.generateFortune(request);
        
        // then
        Fortune fortune = fortuneRepository.findById(fortuneId);
        assertThat(fortuneId).isEqualTo(fortune.getId());
    }
    
    private Member createMember(String nickname, String email) {
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .profileImageURL("https://example.com/profile.png")
            .role(Role.ROLE_USER)
            .build();
    }
    
}