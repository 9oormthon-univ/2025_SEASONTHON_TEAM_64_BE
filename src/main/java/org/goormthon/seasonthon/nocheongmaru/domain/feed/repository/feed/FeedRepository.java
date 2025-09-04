package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import static org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.QFeed.*;
import static org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.QFeedLike.*;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class FeedRepository {

    private final FeedJpaRepository feedJpaRepository;
    private final JPAQueryFactory query;

    public Feed save(Feed entity) { return feedJpaRepository.save(entity); }

    public Optional<Feed> findById(Long id) { return feedJpaRepository.findById(id); }

    public void deleteById(Long id) { feedJpaRepository.deleteById(id); }

    public List<Feed> findFeedsByCursorWithMember(Long cursorId, Pageable pageable) {
        return query.selectFrom(feed)
            .join(feed.member, member).fetchJoin()
            .where(cursorId == null ? null : feed.id.lt(cursorId))
            .orderBy(feed.id.desc())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public Optional<Feed> findByIdWithMember(Long feedId) {
        Feed result = query.selectFrom(feed)
            .join(feed.member, member).fetchJoin()
            .where(feed.id.eq(feedId))
            .fetchOne();
        return Optional.ofNullable(result);
    }

    public List<FeedIdCount> countDistinctMemberByFeedIds(List<Long> feedIds) {
        if (feedIds == null || feedIds.isEmpty()) return List.of();

        return query
            .select(Projections.constructor(
                FeedIdCount.class,
                feedLike.feed.id,
                feedLike.member.id.countDistinct()
            ))
            .from(feedLike)
            .where(feedLike.feed.id.in(feedIds))
            .groupBy(feedLike.feed.id)
            .fetch();
    }

    public long countDistinctMemberByFeedId(Long feedId) {
        Long cnt = query
            .select(feedLike.member.id.countDistinct())
            .from(feedLike)
            .where(feedLike.feed.id.eq(feedId))
            .fetchOne();
        return cnt == null ? 0L : cnt;
    }

    public boolean existsByIdAndMemberId(Long feedId, Long memberId) {
        Integer one = query.selectOne()
            .from(feed)
            .where(feed.id.eq(feedId).and(feed.member.id.eq(memberId)))
            .fetchFirst();
        return one != null;
    }
}
