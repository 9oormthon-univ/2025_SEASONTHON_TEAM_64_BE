package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedLikeJpaRepository extends JpaRepository<FeedLike, Long> {

	boolean existsByFeed_IdAndMember_Id(Long feedId, Long memberId);

	long deleteByFeed_IdAndMember_Id(Long feedId, Long memberId);

	long countByFeed_Id(Long feedId);

	// 🔥 QueryDSL 대체: 피드별 좋아요 전부 삭제(벌크)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from FeedLike fl where fl.feed.id = :feedId")
	int deleteAllByFeedIdInBulk(@Param("feedId") Long feedId);
}
