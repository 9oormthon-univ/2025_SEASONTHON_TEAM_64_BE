package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.MemberAssignmentDto;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.MissionDto;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.AssignmentListResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.MemberTodayMissionResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MissionService {

	private final MemberMissionRepository memberMissionRepo;

	public AssignmentListResponse getAssignmentsByDate(LocalDate target) {
		List<MemberMission> rows = memberMissionRepo.findAllByForDateWithFetch(target);

		List<MemberAssignmentDto> items = rows.stream()
			.map(mm -> {
				Member m = mm.getMember();
				Mission ms = mm.getMission();
				return new MemberAssignmentDto(
					m.getId(),
					m.getNickname(),
					ms.getId(),
					ms.getTitle(),
					mm.getStatus().name()
				);
			})
			.toList();

		return new AssignmentListResponse(target, items);
	}

	public MemberTodayMissionResponse getMemberTodayMission(Long memberId, LocalDate date) {
		MemberMission mm = memberMissionRepo.findByMemberIdAndForDateWithFetch(memberId, date);
		Mission ms = mm.getMission();

		return new MemberTodayMissionResponse(
			memberId,
			date,
			new MissionDto(ms.getId(), ms.getTitle()),
			mm.getStatus().name()
		);
	}
}
