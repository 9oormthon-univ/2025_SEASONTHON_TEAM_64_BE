package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;

import java.util.List;

public interface FeedCustomRepository {
    
    List<FeedResponse> getFeeds(Long memberId, Long lastFeedId);
    
    FeedResponse getFeed(Long memberId, Long feedId);
    
}
