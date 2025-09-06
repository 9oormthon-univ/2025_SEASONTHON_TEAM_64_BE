package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto;

import lombok.Builder;

@Builder
public record MissionDto (
	Long id,
	String title
){}
