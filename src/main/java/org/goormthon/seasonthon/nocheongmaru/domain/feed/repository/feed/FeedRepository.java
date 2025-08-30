package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedRepository {
    
    private final FeedJpaRepository feedJpaRepository;
    
}
