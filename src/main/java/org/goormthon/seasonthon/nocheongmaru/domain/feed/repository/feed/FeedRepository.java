package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.FeedNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    
    public boolean existsByMemberIdAndId(Long memberId, Long feedId) {
        return feedJpaRepository.existsByMemberIdAndId(memberId, feedId);
    }
    
    public void delete(Feed feed) {
        feedJpaRepository.delete(feed);
    }
    
    public List<FeedResponse> getFeeds(Long memberId, Long lastFeedId) {
        return feedJpaRepository.getFeeds(memberId, lastFeedId);
    }
    
    public FeedResponse getFeed(Long memberId, Long feedId) {
        return feedJpaRepository.getFeed(memberId, feedId);
    }
    
    public void deleteAllInBatch() {
        feedJpaRepository.deleteAllInBatch();
    }
}
