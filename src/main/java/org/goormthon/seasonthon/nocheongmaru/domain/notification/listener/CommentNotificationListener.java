package org.goormthon.seasonthon.nocheongmaru.domain.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.CommentCreatedEvent;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentNotificationListener {

    private final NotificationSenderService notificationSenderService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommentCreated(CommentCreatedEvent event) {
        try {
            notificationSenderService.sendCommentNotification(
                event.recipientId(),
                event.commenterNickname(),
                event.feedId()
            );
        } catch (Exception e) {
            log.warn("댓글 알림 처리 실패 recipientId={}, reason={}", event.recipientId(), e.getMessage());
        }
    }
    
}
