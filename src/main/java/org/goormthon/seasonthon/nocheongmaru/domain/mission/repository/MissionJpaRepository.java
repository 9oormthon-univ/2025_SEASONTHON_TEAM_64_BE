package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;


import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionJpaRepository extends JpaRepository<Mission, Long> {
}
