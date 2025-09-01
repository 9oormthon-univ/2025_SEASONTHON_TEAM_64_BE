package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import static org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.QComment.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class CommentRepository {
    
    private final CommentJpaRepository commentJpaRepository;
    private final JPAQueryFactory query;

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

}
