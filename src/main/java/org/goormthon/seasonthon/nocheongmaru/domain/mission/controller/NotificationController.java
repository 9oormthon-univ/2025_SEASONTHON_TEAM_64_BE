package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import java.time.LocalDate;
import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.FcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final FcmService fcmService;
	private final MemberRepository memberRepo;

	@PostMapping("/member/{id}")
	public ResponseEntity<Void> sendToMember(
		@PathVariable Long id,
		@RequestBody Map<String, String> req) {

		Member member = memberRepo.findById(id);

		String title = req.get("title");
		String body = req.getOrDefault("body", "");

		fcmService.sendNotification(member, title, body);

		return ResponseEntity.ok().build();
	}


	@PostMapping("/topic/{topic}")
	public ResponseEntity<Void> sendToTopic(@PathVariable String topic, @RequestBody Map<String, String> req) {
		fcmService.sendToTopic(topic, req.get("title"), req.get("body"));
		return ResponseEntity.ok().build();
	}
}
