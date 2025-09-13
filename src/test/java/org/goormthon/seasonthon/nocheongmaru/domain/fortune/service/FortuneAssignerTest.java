package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune.MemberFortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.fortune.AlreadyAssignFortuneException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FortuneAssignerTest extends IntegrationTestSupport {
    
    @Autowired
    private FortuneAssigner fortuneAssigner;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private FortuneRepository fortuneRepository;
    
    @Autowired
    private MemberFortuneRepository memberFortuneRepository;
    
    @AfterEach
    void tearDown() {
        memberFortuneRepository.deleteAllInBatch();
        fortuneRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("사용자는 배정되지 않은 포춘쿠키 중 랜덤으로 하나를 배정받는다.")
    @Test
    void assignFortune_RandomUnassigned() {
        // given
        Member receiver = createMember("receiver", "receiver@test.com");
        Member s1 = createMember("s1", "s1@test.com");
        Member s2 = createMember("s2", "s2@test.com");
        Member s3 = createMember("s3", "s3@test.com");
        memberRepository.saveAll(List.of(receiver, s1, s2, s3));
        
        var fortunes = List.of(
            createFortune("A", s1),
            createFortune("B", s2),
            createFortune("C", s3)
        );
        fortuneRepository.saveAll(fortunes);
        
        // when
        FortuneResponse response = fortuneAssigner.assignFortune(receiver);
        
        // then
        assertThat(List.of("A", "B", "C")).contains(response.description());
        assertThat(memberFortuneRepository.count()).isEqualTo(1);
    }
    
    @DisplayName("오늘 이미 배정받았다면 예외가 발생한다.")
    @Test
    void assignFortune_AlreadyAssignedToday() {
        // given
        Member receiver = createMember("receiver", "receiver@test.com");
        Member sender = createMember("sender", "sender@test.com");
        memberRepository.saveAll(List.of(receiver, sender));
        
        fortuneRepository.saveAll(List.of(
            createFortune("행운", sender),
            createFortune("또다른 행운", sender)
        ));
        
        fortuneAssigner.assignFortune(receiver);
        
        // when & then
        assertThatThrownBy(() -> fortuneAssigner.assignFortune(receiver))
            .isInstanceOf(AlreadyAssignFortuneException.class);
    }
    
    @DisplayName("같은 포춘쿠키는 여러 사용자에게 배정될 수 있다.")
    @Test
    void assignFortune_SingleFortune_AssignToSecondUserSucceeds() {
        // given
        Member receiver1 = createMember("r1", "r1@test.com");
        Member receiver2 = createMember("r2", "r2@test.com");
        Member sender = createMember("sender", "sender@test.com");
        memberRepository.saveAll(List.of(receiver1, receiver2, sender));
        
        Fortune single = createFortune("단 하나", sender);
        fortuneRepository.saveAll(List.of(single));
        
        // when
        FortuneResponse first = fortuneAssigner.assignFortune(receiver1);
        FortuneResponse second = fortuneAssigner.assignFortune(receiver2);
        
        // then
        assertThat(first.description()).isEqualTo("단 하나");
        assertThat(second.description()).isEqualTo("단 하나");
        assertThat(memberFortuneRepository.count()).isEqualTo(2);
    }
    
    @DisplayName("단 하나의 포춘쿠키로도 여러 사용자의 동시 요청을 모두 처리할 수 있다(중복 배정 허용).")
    @Test
    void assignFortune_ConcurrentManyUsers_SingleFortune_DuplicatesAllowed() throws InterruptedException {
        // given
        Member sender = createMember("sender", "sender@test.com");
        memberRepository.save(sender);
        
        Fortune fortune = createFortune("공통 포춘", sender);
        fortuneRepository.save(fortune);
        
        int userCount = 30;
        List<Member> receivers = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            receivers.add(memberRepository.save(createMember("u" + i, "u" + i + "@test.com")));
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(userCount);
        var errors = Collections.synchronizedList(new ArrayList<Throwable>());
        
        for (Member r : receivers) {
            executor.submit(() -> {
                try {
                    start.await();
                    fortuneAssigner.assignFortune(r);
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            });
        }
        
        start.countDown();
        done.await();
        executor.shutdown();
        
        // then
        assertThat(memberFortuneRepository.count()).isEqualTo(userCount);
    }
    
    private Member createMember(String nickname, String email) {
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .profileImageURL("https://example.com/profile.png")
            .role(Role.ROLE_USER)
            .build();
    }
    
    private Fortune createFortune(String description, Member sender) {
        return Fortune.builder()
            .description(description)
            .sender(sender)
            .build();
    }
}
