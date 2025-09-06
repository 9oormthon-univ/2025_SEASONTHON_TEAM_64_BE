package org.goormthon.seasonthon.nocheongmaru.domain.notification.scheduler;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FortuneNotificationScheduler {

	private final MemberRepository memberRepo;
	private final NotificationService notificationService;

	@Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
	public void sendDailyFortuneNotification() {
		List<Member> members = memberRepo.findAll();

		for (Member member : members) {
			notificationService.createNotification(
				member,
				"오늘의 포춘쿠키 🍪",
				"오늘의 포춘쿠키를 작성하고 열어보세요!",
				NotificationType.FORTUNE
			);
		}
	}
}
