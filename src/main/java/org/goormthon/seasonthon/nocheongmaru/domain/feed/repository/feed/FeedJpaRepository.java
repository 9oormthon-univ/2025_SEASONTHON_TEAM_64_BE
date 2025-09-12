package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedJpaRepository extends JpaRepository<Feed, Long> {
    
    boolean existsByMemberIdAndMissionId(Long memberId, Long missionId);
    
    boolean existsByMemberIdAndId(Long memberId, Long feedId);
    
}
