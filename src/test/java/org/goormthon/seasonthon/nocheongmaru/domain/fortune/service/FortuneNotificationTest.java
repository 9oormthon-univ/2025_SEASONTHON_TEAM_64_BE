package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.InMemoryPushSender;
import org.goormthon.seasonthon.nocheongmaru.MulticastRecord;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationSenderService;
import org.goormthon.seasonthon.nocheongmaru.TestNotificationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestNotificationConfig.class)
class FortuneNotificationTest extends IntegrationTestSupport {
    
    @Autowired
    private NotificationSenderService notificationSenderService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    
    @Autowired
    private InMemoryPushSender pushSender;
    
    @AfterEach
    void tearDown() {
        pushSender.clear();
        notificationJpaRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("포춘쿠키 알림은 디바이스 토큰이 있는 회원에게만 멀티캐스트로 전송된다.")
    @Test
    void sendFortuneToAll() {
        // given
        Member admin = memberRepository.save(createMember("admin@f.com", "admin", Role.ROLE_ADMIN));
        admin.updateDeviceToken("ta");
        memberRepository.save(admin);
        Member user1 = memberRepository.save(createMember("u1@f.com", "u1", Role.ROLE_USER));
        user1.updateDeviceToken("t1");
        memberRepository.save(user1);
        Member user2 = memberRepository.save(createMember("u2@f.com", "u2", Role.ROLE_USER));
        
        // when
        notificationSenderService.sendFortuneToAll();
        
        // then
        assertThat(pushSender.getMulticasts()).hasSize(1);
        MulticastRecord rec = pushSender.getMulticasts().get(0);
        assertThat(rec.tokens()).containsExactlyInAnyOrder("ta", "t1");
        assertThat(rec.data().get("type")).isEqualTo(NotificationType.FORTUNE.name());
        assertThat(rec.title()).isEqualTo("포춘쿠키 도착");
        assertThat(rec.body()).isEqualTo("오늘의 포춘쿠키를 열어보세요.");
        assertThat(notificationJpaRepository.count()).isEqualTo(2);
    }
    
    private Member createMember(String email, String nickname, Role role) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL("profile")
            .role(role)
            .build();
    }
}
