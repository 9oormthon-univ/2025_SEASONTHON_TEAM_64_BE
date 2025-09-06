package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import java.time.LocalDate;
import java.time.ZoneId;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.docs.MissionControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.AssignmentListResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.MemberTodayMissionResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.MissionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class MissionController implements MissionControllerDocs {

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
	private final MissionService missionService;

	@GetMapping("/missions/assignments")
	public ResponseEntity<AssignmentListResponse> getAssignmentsByDate(
		@RequestParam(required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		LocalDate target = (date != null) ? date : LocalDate.now(KST);
		AssignmentListResponse resp = missionService.getAssignmentsByDate(target);
		return ResponseEntity.ok(resp);
	}

	@GetMapping("/members/{memberId}/missions/today")
	public ResponseEntity<MemberTodayMissionResponse> getMemberTodayMission(
		@PathVariable Long memberId
	) {
		LocalDate today = LocalDate.now(KST);
		MemberTodayMissionResponse resp = missionService.getMemberTodayMission(memberId, today);
		return ResponseEntity.ok(resp);
	}
}
