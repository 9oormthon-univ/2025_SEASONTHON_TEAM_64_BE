package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepo;
	private final FcmService fcmService;

	@Transactional
	public void createMissionAssignedNotification(Member member, Mission mission, LocalDate date, NotificationType type) {
		String message = "[" + date + "] 오늘의 미션: " + mission.getTitle();

		// DB 저장
		Notification notification = Notification.builder()
			.member(member)
			.message(message)
			.type(type)
			.isRead(false)
			.createdAt(LocalDateTime.now())
			.build();
		notificationRepo.save(notification);

		// FCM 발송
		fcmService.sendNotification(member, "오늘의 미션", message);
	}

	@Transactional
	public void createNotification(Member receiver, String title, String message, NotificationType type) {
		// 1. DB 저장
		Notification notification = Notification.builder()
			.member(receiver)
			.message(message)
			.type(type)
			.isRead(false)
			.createdAt(LocalDateTime.now())
			.build();

		notificationRepo.save(notification);

		// 2. FCM 발송
		fcmService.sendNotification(receiver, title, message);
	}


	@Transactional(readOnly = true)
	public List<Notification> getNotifications(Long memberId) {
		return notificationRepo.findByMemberIdOrderByCreatedAtDesc(memberId);
	}

	@Transactional
	public void markAsRead(Long notificationId) {
		Notification noti = notificationRepo.findById(notificationId)
			.orElseThrow(() -> new IllegalArgumentException("알림 없음"));
		noti.setRead(true);
	}
}
