package org.goormthon.seasonthon.nocheongmaru.domain.mission.scheduler;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.MissionAssigner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MissionAssignmentScheduler {
    
    private final MissionAssigner missionAssigner;
    
    @Scheduled(cron = "0 0 8 * * *")
    public void assignDailyAndNotify() {
        missionAssigner.assignDailyMission();
    }
    
}

