package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.InMemoryPushSender;
import org.goormthon.seasonthon.nocheongmaru.MulticastRecord;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.TestNotificationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestNotificationConfig.class)
class MissionAssignNotificationTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionAssigner missionAssigner;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    
    @Autowired
    private InMemoryPushSender pushSender;
    
    @AfterEach
    void tearDown() {
        pushSender.clear();
        notificationJpaRepository.deleteAllInBatch();
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("매일 미션 배정 시 USER들(관리자 제외)에게 FCM 멀티캐스트 알림이 전송된다.")
    @Test
    void sendOnMissionAssigned() {
        // given
        Member admin = memberRepository.save(createMember("admin@ma.com", "admin", Role.ROLE_ADMIN));
        Member user1 = memberRepository.save(createMember("u1@ma.com", "u1", Role.ROLE_USER));
        user1.updateDeviceToken("t1");
        memberRepository.save(user1);
        Member user2 = memberRepository.save(createMember("u2@ma.com", "u2", Role.ROLE_USER));
        user2.updateDeviceToken("t2");
        memberRepository.save(user2);
        Member user3 = memberRepository.save(createMember("u3@ma.com", "u3", Role.ROLE_USER)); // 토큰 없음
        
        missionRepository.save(createMission(admin));
        
        // when
        missionAssigner.assignDailyMission();
        
        // then
        assertThat(pushSender.getMulticasts()).hasSize(1);
        MulticastRecord rec = pushSender.getMulticasts().get(0);
        assertThat(rec.tokens()).containsExactlyInAnyOrder("t1", "t2");
        assertThat(rec.data().get("type")).isEqualTo(NotificationType.MISSION.name());
        assertThat(rec.title()).isEqualTo("오늘의 미션 도착");
        assertThat(rec.body()).isEqualTo("오늘의 미션이 도착했어요.");
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
    
    private Mission createMission(Member member) {
        return Mission.builder()
            .description("daily")
            .member(member)
            .build();
    }
}
