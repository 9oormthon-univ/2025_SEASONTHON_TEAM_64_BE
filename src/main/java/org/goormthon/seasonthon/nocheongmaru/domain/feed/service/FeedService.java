package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request.FeedCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedService {
    
    private final MissionValidator missionValidator;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final FeedGenerator feedGenerator;
    
    public Long generateFeed(FeedCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        missionValidator.isTodayMissionAssigned(member.getId());
        
        Mission mission = missionRepository.findById(request.missionId());
        missionValidator.isTodayFeedGenerated(member.getId(), mission.getId());
        
        return feedGenerator.generateFeed(
            member,
            mission,
            request.description(),
            request.imageFile()
        );
    }
    
}
