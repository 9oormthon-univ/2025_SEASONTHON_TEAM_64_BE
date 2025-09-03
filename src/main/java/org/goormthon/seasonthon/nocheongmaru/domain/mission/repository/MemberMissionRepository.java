package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.TodayMissionNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MissionNotFoundException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemberMissionRepository {

	private final MemberMissionJpaRepository memberMissionJpaRepository;

	public boolean existsByMemberIdAndForDate(Long memberId, LocalDate forDate) {
		return memberMissionJpaRepository.existsByMember_IdAndForDate(memberId, forDate);
	}

	public boolean existsByMemberIdAndMissionId(Long memberId, Long missionId) {
		return memberMissionJpaRepository.existsByMember_IdAndMission_Id(memberId, missionId);
	}

	public MemberMission findByMemberIdAndForDate(Long memberId, LocalDate forDate) {
		return memberMissionJpaRepository.findByMember_IdAndForDate(memberId, forDate)
			.orElseThrow(TodayMissionNotFoundException::new);
	}

	public MemberMission save(MemberMission mm) {
		return memberMissionJpaRepository.save(mm);
	}

	public MemberMission findByMemberIdAndForDateWithFetch(Long memberId, LocalDate forDate) {
		return memberMissionJpaRepository.findByMemberIdAndForDateWithFetch(memberId, forDate)
			.orElseThrow(TodayMissionNotFoundException::new);
	}

	public List<MemberMission> findAllByForDateWithFetch(LocalDate forDate) {
		return memberMissionJpaRepository.findAllByForDateWithFetch(forDate);
	}
}
