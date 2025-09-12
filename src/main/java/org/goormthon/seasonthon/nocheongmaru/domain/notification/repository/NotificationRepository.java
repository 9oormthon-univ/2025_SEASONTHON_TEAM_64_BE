package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {
    
    private final NotificationJpaRepository notificationJpaRepository;
    
    public void save(Notification notification) {
        notificationJpaRepository.save(notification);
    }
    
}
