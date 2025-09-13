package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface FortuneJpaRepository extends JpaRepository<Fortune, Long> {
    
    boolean existsBySenderAndForDate(Member sender, LocalDate forDate);
    
    @Query(value = "select * from fortunes order by rand() limit 1", nativeQuery = true)
    Optional<Fortune> findRandomUnassigned();
    
}
