package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FortuneJpaRepository extends JpaRepository<Fortune, Long> {
    
    boolean existsBySenderAndForDate(Member sender, LocalDate forDate);
}
