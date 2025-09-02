package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberPushController {

	private final MemberRepository memberRepository;

	public record FcmTokenRequest(@NotBlank String token) {}

	@PostMapping("/me/fcm-token")
	public ResponseEntity<Map<String,String>> registerFcmToken(
		@RequestBody @Valid FcmTokenRequest request,
		@AuthMemberId Long memberId
	) {
		Member member = memberRepository.findById(memberId);
		member.updateFcmToken(request.token());
		return ResponseEntity.ok(Map.of("message", "FCM 토큰 등록 완료"));
	}
}
