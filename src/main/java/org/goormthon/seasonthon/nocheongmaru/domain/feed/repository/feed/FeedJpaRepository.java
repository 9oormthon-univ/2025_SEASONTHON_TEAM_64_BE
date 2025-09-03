package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import java.util.List;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;


public interface FeedJpaRepository extends JpaRepository<Feed, Long> {

	@EntityGraph(attributePaths = "member")
	List<Feed> findAllByOrderByIdDesc(Pageable pageable);

	@EntityGraph(attributePaths = "member")
	List<Feed> findByIdLessThanOrderByIdDesc(Long cursorId, Pageable pageable);

	@EntityGraph(attributePaths = "member")
	Optional<Feed> findOneWithMemberById(Long id);

	boolean existsByIdAndMember_Id(Long feedId, Long memberId);
	boolean existsByMember_IdAndMission_Id(Long memberId, Long missionId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	void deleteByIdAndMember_Id(Long feedId, Long memberId);

	@Query("""
        select count(distinct fl.member.id)
        from FeedLike fl
        where fl.feed.id = :feedId
        """)
	long countDistinctLikerByFeedId(@Param("feedId") Long feedId);

	@Query("""
        select new org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount(
            fl.feed.id, count(distinct fl.member.id)
        )
        from FeedLike fl
        where fl.feed.id in :feedIds
        group by fl.feed.id
        """)
	List<FeedIdCount> countDistinctLikerGroupByFeedIds(@Param("feedIds") List<Long> feedIds);
}
