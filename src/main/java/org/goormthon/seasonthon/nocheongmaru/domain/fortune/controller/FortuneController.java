package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.docs.FortuneControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.FortuneRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.FortuneService;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.MemberService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fortunes")
public class FortuneController implements FortuneControllerDocs {

	private final FortuneService fortuneService;
	private final MemberService memberService;

	@PostMapping("/send")
	public ResponseEntity<FortuneResponse> sendFortune(
		@AuthMemberId Long memberId,
		@RequestBody FortuneRequest request
	) {
		Member sender = memberService.getById(memberId);
		Fortune fortune = fortuneService.sendFortune(sender, request.description());
		return ResponseEntity.ok(FortuneResponse.from(fortune));
	}

	@PostMapping("/open")
	public ResponseEntity<FortuneResponse> openFortune(
		@AuthMemberId Long memberId
	) {
		Member receiver = memberService.getById(memberId);
		Fortune fortune = fortuneService.openFortune(receiver);
		return ResponseEntity.ok(FortuneResponse.from(fortune));
	}

	@GetMapping("/{fortuneId}")
	public ResponseEntity<FortuneResponse> getFortune(@PathVariable Long fortuneId) {
		return ResponseEntity.ok(fortuneService.getFortune(fortuneId));
	}

	@GetMapping("/cursor")
	public ResponseEntity<List<FortuneResponse>> getFortunes(
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(fortuneService.getFortunes(cursorId, size));
	}
}
