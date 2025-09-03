package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.service.MemberService;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.docs.MemberPushControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.MissionAssignmentService;
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
public class MemberPushController implements MemberPushControllerDocs {

	private final MissionAssignmentService missionAssignmentService;
	private final MemberService memberService;

	@PostMapping("/assign-today")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> assignTodayAndNotifyNow() {
		missionAssignmentService.assignDailyAndNotify();

		return ResponseEntity.ok(
			Map.of(
				"message", "오늘 날짜 기준 미션 배정 및 FCM 발송 트리거 완료",
				"triggeredAt", ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString()
			)
		);
	}

	@PostMapping("/{memberId}/fcm-token")
	public ResponseEntity<Void> registerFcmToken(@PathVariable Long memberId,
		@RequestBody Map<String, String> req) {
		memberService.updateFcmToken(memberId, req.get("token"));
		return ResponseEntity.noContent().build();
	}
}
