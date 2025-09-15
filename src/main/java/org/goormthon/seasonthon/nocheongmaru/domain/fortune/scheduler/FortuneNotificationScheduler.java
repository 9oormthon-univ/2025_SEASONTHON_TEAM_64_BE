package org.goormthon.seasonthon.nocheongmaru.domain.fortune.scheduler;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationSenderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FortuneNotificationScheduler {

    private final NotificationSenderService notificationSenderService;
    
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyFortuneNotification() {
        notificationSenderService.sendFortuneToAll();
    }
    
}

