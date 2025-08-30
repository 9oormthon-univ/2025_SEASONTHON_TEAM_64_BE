package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedLikeRepository {
    
    private final FeedLikeJpaRepository feedLikeJpaRepository;
    
}
