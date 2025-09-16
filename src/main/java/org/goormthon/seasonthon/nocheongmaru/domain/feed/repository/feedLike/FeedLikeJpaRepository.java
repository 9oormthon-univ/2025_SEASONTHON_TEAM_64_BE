package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeJpaRepository extends JpaRepository<FeedLike, Long> {
    
    boolean existsByFeedIdAndMemberId(Long feedId, Long memberId);
    
    long countByFeedIdAndMemberId(Long feedId, Long memberId);
    
    void deleteByFeedIdAndMemberId(Long feedId, Long memberId);
    
    void deleteByFeedId(Long feedId);
    
}
