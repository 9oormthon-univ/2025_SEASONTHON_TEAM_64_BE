package org.goormthon.seasonthon.nocheongmaru.domain.member.controller;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.controller.docs.MemberControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController implements MemberControllerDocs {

	private final MemberService memberService;

	@PostMapping("/{memberId}/fcm-token")
	public ResponseEntity<Map<String, String>> registerFcmToken(
		@PathVariable Long memberId,
		@RequestBody Map<String, String> request
	) {
		String token = request.get("token");
		if (token == null || token.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("message", "토큰이 비어 있습니다."));
		}

		memberService.updateFcmToken(memberId, token);
		return ResponseEntity.ok(Map.of("message", "FCM 토큰이 정상적으로 등록되었습니다."));
	}
}
