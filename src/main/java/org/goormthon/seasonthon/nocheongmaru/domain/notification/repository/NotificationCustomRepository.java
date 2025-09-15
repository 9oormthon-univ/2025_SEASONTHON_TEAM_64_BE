package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationCustomRepository {
    
    List<NotificationResponse> getNotifications(Long memberId, Long lastNotificationId);
    
}
