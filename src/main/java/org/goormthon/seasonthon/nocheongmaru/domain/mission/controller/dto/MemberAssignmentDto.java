package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto;

import lombok.Builder;

@Builder
public record MemberAssignmentDto (
	Long memberId,
	String memberName,
	Long missionId,
	String missionTitle,
	String status
)
{}
