package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import java.util.List;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedJpaRepository extends JpaRepository<Feed, Long> {

	// 커서 기반 페이지네이션을 위한 네이티브 쿼리
	@Query(value = "SELECT * FROM feeds f WHERE (:cursorId IS NULL OR f.feed_id < :cursorId) " +
		"ORDER BY f.feed_id DESC", nativeQuery = true)
	List<Feed> findFeedsByCursor(Long cursorId, Pageable pageable);

	/**
	 * 게시물들의 좋아요 누른 수 리스트로 조회 (리스트) -> Feed가 많아지면 단일조회 쿼리가 많이 발생하는것을 방지하여
	 * 단일과 리스트로 분리해서 구현
	 */
	@Query("""
		select fl.feed.id as feedId, count(distinct fl.member.id) as count
		from FeedLike fl
		where fl.feed.id in :feedIds
		group by fl.feed.id
	""")
	List<FeedIdCount> countDistinctMemberByFeedIds(@Param("feedIds") List<Long> feedIds);

	// 좋아요 누른 회원 기준(중복 방지) distinct 조건 쿼리 (단일조회)
	@Query("select count(distinct fl.member.id) from FeedLike fl where fl.feed.id = :feedId")
	long countDistinctMemberByFeedId(@Param("feedId") Long feedId);

	/**
	 * Feed와 Member를 함께 효율적으로 가져오기 위한 Cursor 쿼리
	 */
	@Query("""
       select f
       from Feed f
       join fetch f.member m
       where (:cursorId is null or f.id < :cursorId)
       order by f.id desc
    """)
	List<Feed> findFeedsByCursorWithMember(@Param("cursorId") Long cursorId, Pageable pageable);

	/**
	 * Feed와 Member를 함께 효율적으로 가져오기 위한 쿼리
	 */
	@Query("""
       select f
       from Feed f
       join fetch f.member m
       where f.id = :feedId
    """)
	Optional<Feed> findByIdWithMember(@Param("feedId") Long feedId);

	// (옵션) 내가 좋아요 눌렀는지 -> 추후 기능개발을 위한 코드
	boolean existsByIdAndMemberId(Long feedId, Long memberId);
}
