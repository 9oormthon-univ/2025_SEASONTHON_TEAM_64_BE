package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.AlreadyUploadedTodayException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.TodayMissionNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MissionValidator {
    
    private final FeedRepository feedRepository;
    private final MemberMissionRepository memberMissionRepository;
    
    // 오늘 미션이 할당되었는지
    public void isTodayMissionAssigned(Long memberId) {
        if(!memberMissionRepository.existsByMemberIdAndForDate(memberId)) {
            throw new TodayMissionNotFoundException();
        }
    }
    
    // 이미 오늘 미션으로 피드를 생성했는지
    public void isTodayFeedGenerated(Long memberId, Long missionId) {
        if(feedRepository.existsByMemberIdAndMissionId(memberId, missionId)) {
            throw new AlreadyUploadedTodayException();
        }
    }
    
}
