package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.push.PushSender;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSenderService {
    
    private final PushSender pushSender;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMissionAssigned(List<Long> recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) return;
        List<Member> recipients = memberRepository.findAllByIds(recipientIds);
        List<Member> recipientsWithToken = recipients.stream()
            .filter(m -> m.getDeviceToken() != null && !m.getDeviceToken().isBlank())
            .toList();
        List<String> tokens = recipientsWithToken.stream()
            .map(Member::getDeviceToken)
            .toList();
        if (tokens.isEmpty()) return;
        
        String title = "오늘의 미션 도착";
        String body = "오늘의 미션이 도착했어요.";
        Map<String, String> data = Map.of("type", NotificationType.MISSION.name());
        
        pushSender.sendMulticast(tokens, title, body, data);
        recipients.forEach(m -> persist(m, NotificationType.MISSION, body));
    }
    
    private void persist(Member member, NotificationType type, String message) {
        Notification notification = Notification.builder()
            .member(member)
            .type(type)
            .message(message)
            .build();
        notificationRepository.save(notification);
    }
    
}

