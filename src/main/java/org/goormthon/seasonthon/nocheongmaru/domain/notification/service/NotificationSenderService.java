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
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendCommentNotification(Long recipientId, String commenterNickname, Long feedId) {
        Member recipient = memberRepository.findById(recipientId);
        String token = recipient.getDeviceToken();
        if (token == null || token.isBlank()) return;
        
        String title = "댓글";
        String body = commenterNickname + " 님이 내 피드에 댓글을 달았어요.";
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.COMMENT.name());
        data.put("feedId", String.valueOf(feedId));
        
        pushSender.sendTo(token, title, body, data);
        persist(recipient, NotificationType.COMMENT, body);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendLikeNotification(Long recipientId, String likerNickname, Long feedId) {
        Member recipient = memberRepository.findById(recipientId);
        String token = recipient.getDeviceToken();
        if (token == null || token.isBlank()) return;
        
        String title = "공감";
        String body = likerNickname + " 님이 내 피드에 공감했어요.";
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.LIKE.name());
        data.put("feedId", String.valueOf(feedId));
        
        pushSender.sendTo(token, title, body, data);
        persist(recipient, NotificationType.LIKE, body);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendFortuneToAll() {
        List<Member> recipients = memberRepository.findAllWithDeviceToken();
        List<String> tokens = recipients.stream()
            .map(Member::getDeviceToken)
            .filter(t -> t != null && !t.isBlank())
            .toList();
        if (tokens.isEmpty()) return;
        
        String title = "포춘쿠키 도착";
        String body = "오늘의 포춘쿠키를 열어보세요.";
        Map<String, String> data = Map.of("type", NotificationType.FORTUNE.name());
        
        pushSender.sendMulticast(tokens, title, body, data);
        recipients.forEach(m -> persist(m, NotificationType.FORTUNE, body));
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

