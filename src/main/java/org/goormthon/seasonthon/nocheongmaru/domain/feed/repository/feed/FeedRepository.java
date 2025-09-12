package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.FeedNotFoundException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedRepository {

    private final FeedJpaRepository feedJpaRepository;
    
    
    public boolean existsByMemberIdAndMissionId(Long memberId, Long missionId) {
        return feedJpaRepository.existsByMemberIdAndMissionId(memberId, missionId);
    }
    
    public void save(Feed feed) {
        feedJpaRepository.save(feed);
    }
    
    public Feed findById(Long feedId) {
        return feedJpaRepository.findById(feedId)
            .orElseThrow(FeedNotFoundException::new);
    }
}
