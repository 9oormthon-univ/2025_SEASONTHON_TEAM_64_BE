package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {
    
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    
    @Transactional(readOnly = true)
    public boolean hasUnreadNotification(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return notificationRepository.existsByMemberIdAndIsRead(member.getId());
    }
    
    @Transactional
    public List<NotificationResponse> getNotifications(Long memberId, Long lastNotificationId) {
        Member member = memberRepository.findById(memberId);
        List<NotificationResponse> notifications = notificationRepository.getNotifications(member.getId(), lastNotificationId);
        
        notificationRepository.markAllAsIsRead(member.getId());
        return notifications;
    }
    
}
