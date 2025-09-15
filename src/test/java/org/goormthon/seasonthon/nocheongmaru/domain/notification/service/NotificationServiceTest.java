package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    
    @AfterEach
    void tearDown() {
        notificationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("읽지 않은 알림이 있으면 true를 반환한다.")
    @Test
    void hasUnreadNotification_NotRead() {
        // given
        Member member = Member.builder()
            .email("user@test.com")
            .nickname("user")
            .role(Role.ROLE_USER)
            .build();
        memberRepository.save(member);
        
        Notification notification = Notification.builder()
            .member(member)
            .type(NotificationType.FORTUNE)
            .message("You have a new fortune!")
            .build();
        notificationRepository.save(notification);
        
        // when
        boolean result = notificationService.hasUnreadNotification(member.getId());
        
        // then
        assertThat(result).isTrue();
    }
    
    @DisplayName("읽지 않은 알림이 없으면 false를 반환한다.")
    @Test
    void hasUnreadNotification_Read() {
        // given
        Member member = Member.builder()
            .email("user@test.com")
            .nickname("user")
            .role(Role.ROLE_USER)
            .build();
        memberRepository.save(member);
        
        Notification notification = Notification.builder()
            .member(member)
            .type(NotificationType.FORTUNE)
            .message("You have a new fortune!")
            .build();
        notification.markAsRead();
        notificationRepository.save(notification);
        
        // when
        boolean result = notificationService.hasUnreadNotification(member.getId());
        
        // then
        assertThat(result).isFalse();
    }
    
    @DisplayName("알림 목록을 조회하면 최신순으로 최대 10개를 반환하고, 모든 미읽음 알림을 읽음 처리한다.")
    @Test
    void getNotifications_FirstPage_MarkAllRead() {
        // given
        Member member = Member.builder()
            .email("user2@test.com")
            .nickname("user2")
            .role(Role.ROLE_USER)
            .build();
        memberRepository.save(member);
        
        for (int i = 1; i <= 3; i++) {
            notificationRepository.save(
                Notification.builder()
                    .member(member)
                    .type(NotificationType.MISSION)
                    .message("m" + i)
                    .build()
            );
        }
        
        // when
        List<NotificationResponse> responses = notificationService.getNotifications(member.getId(), null);
        
        // then
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(3);
        
        boolean hasUnread = notificationRepository.existsByMemberIdAndIsRead(member.getId());
        assertThat(hasUnread).isFalse();
    }
    
    @DisplayName("lastNotificationId 커서를 전달하면 해당 ID보다 오래된 알림들에서 최대 10개를 반환한다.")
    @Test
    void getNotifications_WithCursor() {
        // given
        Member member = Member.builder()
            .email("user3@test.com")
            .nickname("user3")
            .role(Role.ROLE_USER)
            .build();
        memberRepository.save(member);
        
        List<Notification> saved = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Notification n = Notification.builder()
                .member(member)
                .type(NotificationType.FORTUNE)
                .message("msg-" + i)
                .build();
            notificationRepository.save(n);
            saved.add(n);
        }
        saved.sort(Comparator.comparingLong(Notification::getId));
        Long newestId = saved.getLast().getId();
        
        // when
        List<NotificationResponse> responses = notificationService.getNotifications(member.getId(), newestId);
        
        // then
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(10);
        
        boolean hasUnread = notificationRepository.existsByMemberIdAndIsRead(member.getId());
        assertThat(hasUnread).isFalse();
    }
    
}