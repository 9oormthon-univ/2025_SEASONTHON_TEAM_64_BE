package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.NotificationResponse;
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
		String message = "[" + date + "] 오늘의 미션: " + mission.getDescription();

		Notification notification = Notification.builder()
			.member(member)
			.message(message)
			.type(type)
			.isRead(false)
			.createdAt(LocalDateTime.now())
			.build();
		notificationRepo.save(notification);

		fcmService.sendNotification(member, "오늘의 미션", message);
	}

	@Transactional
	public void createNotification(Member receiver, String title, String message, NotificationType type) {
		Notification notification = Notification.builder()
			.member(receiver)
			.message(message)
			.type(type)
			.isRead(false)
			.createdAt(LocalDateTime.now())
			.build();

		notificationRepo.save(notification);

		fcmService.sendNotification(receiver, title, message);
	}

	@Transactional(readOnly = true)
	public List<NotificationResponse> getNotifications(Long memberId) {
		return notificationRepo.findByMemberIdOrderByCreatedAtDesc(memberId).stream()
			.map(n -> new NotificationResponse(
				n.getId(),
				n.getMessage(),
				n.getType(),
				n.isRead(),
				n.getCreatedAt(),
				n.getMember().getNickname() // 트랜잭션 안이라 Lazy 로딩 가능
			))
			.toList();
	}

	@Transactional
	public void markAsRead(Long notificationId) {
		Notification noti = notificationRepo.findById(notificationId)
			.orElseThrow(() -> new IllegalArgumentException("알림 없음"));
		noti.setRead(true);
	}
}
