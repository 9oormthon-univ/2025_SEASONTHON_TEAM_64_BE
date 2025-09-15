package org.goormthon.seasonthon.nocheongmaru.domain.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.FeedLikedEvent;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class FeedLikeNotificationListener {

    private final NotificationSenderService notificationSenderService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFeedLiked(FeedLikedEvent event) {
        try {
            notificationSenderService.sendLikeNotification(
                event.recipientId(),
                event.likerNickname(),
                event.feedId()
            );
        } catch (Exception e) {
            log.warn("좋아요 알림 처리 실패 recipientId={}, reason={}", event.recipientId(), e.getMessage());
        }
    }
    
}

