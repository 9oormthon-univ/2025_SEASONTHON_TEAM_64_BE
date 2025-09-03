package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response;

import java.time.LocalDate;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.MissionDto;

import lombok.Builder;

@Builder
public record MemberTodayMissionResponse (
	Long memberId,
	LocalDate date,
	MissionDto mission,
	String status
) {}
