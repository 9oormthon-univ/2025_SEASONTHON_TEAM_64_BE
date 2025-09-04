package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.NotificationService;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.AlreadyOpenException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.AlreadyWrittenException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FortuneNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FortuneService {

	private final FortuneRepository fortuneRepo;
	private final NotificationService notificationService;
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	@Transactional
	public Fortune sendFortune(Member sender, String description) {
		LocalDate today = LocalDate.now(KST);

		if (fortuneRepo.existsBySenderAndCreatedAt(sender, today)) {
			throw new AlreadyWrittenException();
		}

		Fortune fortune = Fortune.builder()
			.description(description)
			.sender(sender)
			.build();

		Fortune saved = fortuneRepo.save(fortune);

		// ✅ 알림: 오늘의 포춘쿠키 작성 완료
		notificationService.createNotification(
			sender,
			"포춘쿠키 작성 완료 🍪",
			"오늘의 포춘쿠키를 작성했습니다!",
			NotificationType.FORTUNE
		);

		return saved;
	}

	@Transactional
	public Fortune openFortune(Member receiver) {
		LocalDate today = LocalDate.now(KST);

		List<Fortune> fortunes = fortuneRepo.findRandomByTodayExcludingSender(receiver, today);
		if (fortunes.isEmpty()) {
			throw new AlreadyOpenException();
		}

		Fortune opened = fortunes.get(0);

		// ✅ 알림: 오늘의 포춘쿠키 열람 완료
		notificationService.createNotification(
			receiver,
			"포춘쿠키가 도착했습니다 🎁",
			"오늘의 포춘쿠키를 확인했습니다!",
			NotificationType.FORTUNE
		);

		return opened;
	}

	@Transactional(readOnly = true)
	public FortuneResponse getFortune(Long fortuneId) {
		Fortune fortune = fortuneRepo.findById(fortuneId)
			.orElseThrow(FortuneNotFoundException::new);
		return FortuneResponse.from(fortune);
	}

	@Transactional(readOnly = true)
	public List<FortuneResponse> getFortunes(Long cursorId, int size) {
		return fortuneRepo.findByCursor(cursorId, size).stream()
			.map(FortuneResponse::from)
			.toList();
	}
}
