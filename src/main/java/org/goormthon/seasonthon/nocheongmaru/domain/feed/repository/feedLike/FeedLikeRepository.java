package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedLikeRepository {
    
    private final FeedLikeJpaRepository feedLikeJpaRepository;
    
    public void deleteAllInBatch() {
        feedLikeJpaRepository.deleteAllInBatch();
    }
    
    public void save(FeedLike feedLike) {
        feedLikeJpaRepository.save(feedLike);
    }
    
    public boolean existsByFeedIdAndMemberId(Long feedId, Long memberId) {
        return feedLikeJpaRepository.existsByFeedIdAndMemberId(feedId, memberId);
    }
    
    public void deleteByFeedIdAndMemberId(Long feedId, Long memberId) {
        feedLikeJpaRepository.deleteByFeedIdAndMemberId(feedId, memberId);
    }
    
    public long countByFeedIdAndMemberId(Long feedId, Long memberId) {
        return feedLikeJpaRepository.countByFeedIdAndMemberId(feedId, memberId);
    }
    
    public void deleteByFeedId(Long feedId) {
        feedLikeJpaRepository.deleteByFeedId(feedId);
    }
    
}
