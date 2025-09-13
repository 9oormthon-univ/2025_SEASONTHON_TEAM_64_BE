package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FeedReader {
    
    private final FeedRepository feedRepository;
    
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(Long memberId, Long lastFeedId) {
        return feedRepository.getFeeds(memberId, lastFeedId);
    }
    
    @Transactional(readOnly = true)
    public FeedResponse getFeed(Long memberId, Long feedId) {
        return feedRepository.getFeed(memberId, feedId);
    }
    
}
