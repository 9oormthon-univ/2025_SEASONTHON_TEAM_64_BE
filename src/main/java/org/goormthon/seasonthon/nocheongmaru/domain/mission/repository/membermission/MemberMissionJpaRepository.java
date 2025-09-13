package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberMissionJpaRepository extends JpaRepository<MemberMission, Long> {
    
    Optional<MemberMission> findByMemberIdAndForDate(Long memberId, LocalDate forDate);
    
    @Query("""
        select member_mission
        from MemberMission member_mission
        join fetch member_mission.member member
        join fetch member_mission.mission mission
        where member_mission.forDate = :forDate
        order by member.id asc
        """)
    List<MemberMission> findAllByForDateWithFetch(@Param("forDate") LocalDate forDate);
    
    @Query("select distinct mm.mission.id from MemberMission mm where mm.member.id = :memberId")
    List<Long> findAllMissionIdsByMemberId(@Param("memberId") Long memberId);
    
    boolean existsByMemberIdAndForDate(Long memberId, LocalDate forDate);
    
}
