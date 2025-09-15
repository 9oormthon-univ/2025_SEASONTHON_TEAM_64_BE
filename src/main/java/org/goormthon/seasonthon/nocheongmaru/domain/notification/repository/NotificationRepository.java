package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {
    
    private final NotificationJpaRepository notificationJpaRepository;
    
    public void save(Notification notification) {
        notificationJpaRepository.save(notification);
    }
    
    public boolean existsByMemberIdAndIsRead(Long memberId) {
        return notificationJpaRepository.existsByMemberIdAndIsRead(memberId);
    }
    
    public void deleteAllInBatch() {
        notificationJpaRepository.deleteAllInBatch();
    }
    
    public List<NotificationResponse> getNotifications(Long id, Long lastNotificationId) {
        return notificationJpaRepository.getNotifications(id, lastNotificationId);
    }
    
    public void markAllAsIsRead(Long id) {
        notificationJpaRepository.markAllAsIsRead(id);
    }
    
}
