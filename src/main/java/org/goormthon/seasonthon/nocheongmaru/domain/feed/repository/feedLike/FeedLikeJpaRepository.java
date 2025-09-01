package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeJpaRepository extends JpaRepository<FeedLike, Long> {
	// 파생 쿼리 메서드 (중복 좋아요 방지 전제)
	boolean existsByFeed_IdAndMember_Id(Long feedId, Long memberId);

	long deleteByFeed_IdAndMember_Id(Long feedId, Long memberId);

	long countByFeed_Id(Long feedId);
}
