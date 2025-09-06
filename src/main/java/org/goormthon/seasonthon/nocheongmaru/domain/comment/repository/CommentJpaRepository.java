package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	@EntityGraph(attributePaths = "member")
	@Query("""
        select c from Comment c
        join c.member m
        where c.feed.id = :feedId
        order by c.id desc
        """)
	List<Comment> findByFeedIdOrderByIdDescWithMember(
		@Param("feedId") Long feedId,
		Pageable pageable
	);

	@EntityGraph(attributePaths = "member")
	@Query("""
        select c from Comment c
        join c.member m
        where c.feed.id = :feedId
          and c.id < :cursorId
        order by c.id desc
        """)
	List<Comment> findByFeedIdAndIdLessThanOrderByIdDescWithMember(
		@Param("feedId") Long feedId,
		@Param("cursorId") Long cursorId,
		Pageable pageable
	);

	boolean existsByIdAndMemberId(Long id, Long memberId);

	long countByFeedId(Long feedId);

	@Query("""
        select new org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount(
            c.feed.id, count(c)
        )
        from Comment c
        where c.feed.id in :feedIds
        group by c.feed.id
        """)
	List<org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount>
	countGroupByFeedIds(@Param("feedIds") List<Long> feedIds);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	long deleteByFeedId(Long feedId);
}
