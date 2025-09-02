package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import static org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.QComment.*;
import static org.goormthon.seasonthon.nocheongmaru.domain.member.entity.QMember.*;

import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.CommentNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class CommentRepository {
    
    private final JPAQueryFactory query;
    private final EntityManager em;
    private final CommentJpaRepository commentJpaRepository;

    public Comment save(Comment entity) {
        return commentJpaRepository.save(entity);
    }

    public Comment findById(Long id) {
        return commentJpaRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }

    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return commentJpaRepository.existsById(id);
    }

    public List<Comment> findCommentsByCursorWithMember(Long feedId, Long cursorId, Pageable pageable) {
        return query.selectFrom(comment)
            .join(comment.member, member).fetchJoin()
            .where(
                comment.feed.id.eq(feedId),
                cursorId == null ? null : comment.id.lt(cursorId)
            )
            .orderBy(comment.id.desc())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public boolean existsByIdAndMemberId(Long commentId, Long memberId) {
        Integer one = query.selectOne()
            .from(comment)
            .where(comment.id.eq(commentId).and(comment.member.id.eq(memberId)))
            .fetchFirst();
        return one != null;
    }

    public List<FeedIdCount> countByFeedIds(List<Long> feedIds) {
        if (feedIds == null || feedIds.isEmpty()) return List.of();

        return query
            .select(Projections.constructor(
                FeedIdCount.class,
                comment.feed.id,
                comment.id.count()
            ))
            .from(comment)
            .where(comment.feed.id.in(feedIds))
            .groupBy(comment.feed.id)
            .fetch();
    }

    public long countByFeedId(Long feedId) {
        Long cnt = query
            .select(comment.id.count())
            .from(comment)
            .where(comment.feed.id.eq(feedId))
            .fetchOne();
        return cnt == null ? 0L : cnt;
    }

    public long deleteAllByFeedId(Long feedId) {
        long deleted = query
            .delete(comment)
            .where(comment.feed.id.eq(feedId))
            .execute();
        em.clear();
        return deleted;
    }

}
