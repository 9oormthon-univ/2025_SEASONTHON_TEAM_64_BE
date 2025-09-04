package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FortuneJpaRepository extends JpaRepository<Fortune, Long> {

	boolean existsBySenderAndCreatedAt(Member sender, LocalDate createdDate);

	@Query("SELECT f FROM Fortune f WHERE f.sender <> :sender AND DATE(f.createdAt) = :today ORDER BY function('RAND')")
	List<Fortune> findRandomByTodayExcludingSender(@Param("sender") Member sender, @Param("today") LocalDate today);

	@Query("SELECT f FROM Fortune f ORDER BY f.id DESC LIMIT :size")
	List<Fortune> findTopN(@Param("size") int size);

	@Query("SELECT f FROM Fortune f WHERE f.id < :cursorId ORDER BY f.id DESC LIMIT :size")
	List<Fortune> findNextPage(@Param("cursorId") Long cursorId, @Param("size") int size);
}
