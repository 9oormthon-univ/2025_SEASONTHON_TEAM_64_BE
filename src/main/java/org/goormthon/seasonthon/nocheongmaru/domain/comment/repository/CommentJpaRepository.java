package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.dto.FeedIdCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	long countByFeedId(Long feedId);

	// FeedId별로 댓글달린 수를 조회
	@Query("""
        select c.feed.id as feedId, count(c.id) as count
        from Comment c
        where c.feed.id in :feedIds
        group by c.feed.id
    """)
	List<FeedIdCount> countByFeedIds(@Param("feedIds") List<Long> feedIds);
}
