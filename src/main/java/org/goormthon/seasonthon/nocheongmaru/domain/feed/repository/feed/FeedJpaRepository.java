package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedJpaRepository extends JpaRepository<Feed, Long> {
}
