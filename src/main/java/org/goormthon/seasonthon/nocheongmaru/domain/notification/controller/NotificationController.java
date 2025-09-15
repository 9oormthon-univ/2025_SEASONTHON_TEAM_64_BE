package org.goormthon.seasonthon.nocheongmaru.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationService;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<Boolean> hasUnreadNotification(
        @AuthMemberId Long memberId
    ) {
        boolean hasUnread = notificationService.hasUnreadNotification(memberId);
        return ResponseEntity.ok(hasUnread);
    }
    
    @PutMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
        @AuthMemberId Long memberId,
        @RequestParam(required = false) Long lastNotificationId
    ) {
        List<NotificationResponse> notifications = notificationService.getNotifications(memberId, lastNotificationId);
        return ResponseEntity.ok(notifications);
    }
    
}
