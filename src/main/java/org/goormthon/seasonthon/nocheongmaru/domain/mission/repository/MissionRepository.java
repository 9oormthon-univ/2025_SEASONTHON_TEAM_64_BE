package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MissionRepository {
    
    private final MissionJpaRepository missionJpaRepository;
    
}
