package org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
    Long id,
    
    NotificationType type,
    
    String message,
    
    boolean isRead,
    
    LocalDateTime createdAt
    
) {
}
