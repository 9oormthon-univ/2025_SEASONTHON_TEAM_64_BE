package org.goormthon.seasonthon.nocheongmaru.domain.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.MissionAssignedEvent;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionAssignedNotificationListener {

    private final NotificationSenderService notificationSenderService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMissionAssigned(MissionAssignedEvent event) {
        try {
            notificationSenderService.sendMissionAssigned(event.recipientIds());
        } catch (Exception e) {
            log.warn("미션 배정 알림 처리 실패 recipients={}, reason={}", event.recipientIds().size(), e.getMessage());
        }
    }
    
}

