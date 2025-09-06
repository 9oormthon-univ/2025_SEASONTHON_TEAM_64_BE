package org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;

public record MissionResponse(Long id, String title) {
	public static MissionResponse from(Mission mission) {
		return new MissionResponse(
			mission.getId(),
			mission.getTitle()
		);
	}
}
