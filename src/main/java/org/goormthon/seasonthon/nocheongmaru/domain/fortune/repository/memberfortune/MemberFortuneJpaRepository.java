package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.MemberFortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MemberFortuneJpaRepository extends JpaRepository<MemberFortune, Long> {
    
    boolean existsByMemberAndAssignedAt(Member member, LocalDate assignedAt);

}
