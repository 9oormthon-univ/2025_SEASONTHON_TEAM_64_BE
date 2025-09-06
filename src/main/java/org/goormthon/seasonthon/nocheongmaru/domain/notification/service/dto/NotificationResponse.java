package org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto;

import java.time.LocalDateTime;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;

public record NotificationResponse (
	Long id,
	String message,
	NotificationType type,
	boolean isRead,
	LocalDateTime createdAt,
	String memberNickname
) {}
