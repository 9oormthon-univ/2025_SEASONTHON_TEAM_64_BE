package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;

public record FortuneResponse(
	Long id,
	String description
) {
	public static FortuneResponse from(Fortune fortune) {
		return new FortuneResponse(
			fortune.getId(),
			fortune.getDescription()
		);
	}
}