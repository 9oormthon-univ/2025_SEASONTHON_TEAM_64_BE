package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission;


import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MissionJpaRepository extends JpaRepository<Mission, Long> {

	List<Mission> findAllByOrderByIdDesc(Pageable pageable);

	List<Mission> findByIdLessThanOrderByIdDesc(Long cursorId, Pageable pageable);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from Mission m")
	int deleteAllInBulk();
}
