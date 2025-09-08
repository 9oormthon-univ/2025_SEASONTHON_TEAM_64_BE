package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberMissionJpaRepository extends JpaRepository<MemberMission, Long> {
	boolean existsByMemberIdAndForDate(Long memberId, LocalDate forDate);

	boolean existsByMemberIdAndMissionId(Long memberId, Long missionId);
 
	Optional<MemberMission> findByMemberIdAndForDate(Long memberId, LocalDate forDate);

	@Query("""
	       select mm
	       from MemberMission mm
	       join fetch mm.member m
	       join fetch mm.mission ms
	       where m.id = :memberId and mm.forDate = :forDate
	       """)
	Optional<MemberMission> findByMemberIdAndForDateWithFetch(
		@Param("memberId") Long memberId,
		@Param("forDate") LocalDate forDate
	);

	@Query("""
	       select mm
	       from MemberMission mm
	       join fetch mm.member m
	       join fetch mm.mission ms
	       where mm.forDate = :forDate
	       order by m.id asc
	       """)
	List<MemberMission> findAllByForDateWithFetch(@Param("forDate") LocalDate forDate);

}
