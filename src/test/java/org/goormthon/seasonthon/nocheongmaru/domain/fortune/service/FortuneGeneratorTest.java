package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.fortune.AlreadyGenerateFortuneException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FortuneGeneratorTest extends IntegrationTestSupport {
    
    @Autowired
    private FortuneGenerator fortuneGenerator;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private FortuneRepository fortuneRepository;
    
    @AfterEach
    void tearDown() {
        fortuneRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("오늘의 포춘쿠키를 생성한다.")
    @Test
    void generateFortune() {
        // given
        String description = "행운이 찾아올 거예요!";
        
        Member member = createMember("user", "user@test.com");
        memberRepository.save(member);
        
        // when
        Long fortuneId = fortuneGenerator.generateFortune(member, description);
        
        // then
        Fortune fortune = fortuneRepository.findById(fortuneId);
        assertThat(fortuneId).isEqualTo(fortune.getId());
    }
    
    @DisplayName("오늘 이미 포춘쿠키를 생성했다면 예외가 발생한다.")
    @Test
    void generateFortune_AlreadyGenerated() {
        // given
        String description = "행운이 찾아올 거예요!";
        
        Member member = createMember("user", "user@test.com");
        memberRepository.save(member);
        
        fortuneGenerator.generateFortune(member, description);
        
        // when & then
        assertThatThrownBy(() -> fortuneGenerator.generateFortune(member, description))
            .isInstanceOf(AlreadyGenerateFortuneException.class);
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