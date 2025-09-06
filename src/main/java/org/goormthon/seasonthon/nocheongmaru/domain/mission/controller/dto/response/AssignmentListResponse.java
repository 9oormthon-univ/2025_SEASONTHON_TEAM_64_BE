package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.MemberAssignmentDto;

import lombok.Builder;

@Builder
public record AssignmentListResponse (
	LocalDate date,
	List<MemberAssignmentDto> assignments
) {}
