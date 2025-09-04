package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import java.util.List;
import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.MissionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
public class MissionAdminController {

	private final MissionRepository missionRepo;

	@PostMapping
	public ResponseEntity<Mission> createMission(@RequestBody Map<String, String> req) {
		String title = req.get("title");
		Mission mission = Mission.builder()
			.title(title)
			.build();
		missionRepo.save(mission);
		return ResponseEntity.ok(mission);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
		missionRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Mission>> listMissions() {
		return ResponseEntity.ok(missionRepo.findAll());
	}
}
