package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedJpaRepository extends JpaRepository<Feed, Long> {

	boolean existsByMember_IdAndMission_Id(Long memberId, Long missionId);
	void deleteByIdAndMember_Id(Long feedId, Long memberId);
}
