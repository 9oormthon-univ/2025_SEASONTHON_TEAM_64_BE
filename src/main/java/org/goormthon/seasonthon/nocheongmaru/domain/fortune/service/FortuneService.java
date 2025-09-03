package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
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
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	@Transactional
	public Fortune sendFortune(Member sender, String description) {
		LocalDate today = LocalDate.now(KST);

		if (fortuneRepo.existsBySenderAndCreatedDate(sender, today)) {
			throw new AlreadyWrittenException();
		}

		Fortune fortune = Fortune.builder()
			.description(description)
			.sender(sender)
			.build();

		return fortuneRepo.save(fortune);
	}

	@Transactional
	public Fortune openFortune(Member receiver) {
		LocalDate today = LocalDate.now(KST);

		List<Fortune> fortunes = fortuneRepo.findRandomByTodayExcludingSender(receiver, today);
		if (fortunes.isEmpty()) {
			throw new AlreadyOpenException();
		}
		return fortunes.get(0);
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
