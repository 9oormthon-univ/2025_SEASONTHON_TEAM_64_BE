package org.goormthon.seasonthon.nocheongmaru.domain.notification.controller;

import java.util.List;
import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.FcmService;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/{memberId}")
	public ResponseEntity<List<Notification>> list(@PathVariable Long memberId) {
		return ResponseEntity.ok(notificationService.getNotifications(memberId));
	}

	@PatchMapping("/{id}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return ResponseEntity.noContent().build();
	}
}
