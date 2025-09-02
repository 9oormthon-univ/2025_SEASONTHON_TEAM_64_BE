package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMissionJpaRepository extends JpaRepository<MemberMission, Long> {
	boolean existsByMember_IdAndForDate(Long memberId, LocalDate forDate);

	boolean existsByMember_IdAndMission_Id(Long memberId, Long missionId);

	Optional<MemberMission> findByMember_IdAndForDate(Long memberId, LocalDate forDate);
}
