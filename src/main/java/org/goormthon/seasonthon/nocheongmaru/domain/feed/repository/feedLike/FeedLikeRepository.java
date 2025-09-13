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
    
}
