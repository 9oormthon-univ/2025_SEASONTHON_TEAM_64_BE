package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import static org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.QFeed.*;
import static org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.QFeedLike.*;
import static org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.QMember.*;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class FeedRepository {

    private final FeedJpaRepository feedJpaRepository;
    private final JPAQueryFactory query;

    /* ========= 기본 CRUD 위임 ========= */
    @Transactional
    public Feed save(Feed entity) { return feedJpaRepository.save(entity); }

    @Transactional(readOnly = true)
    public Optional<Feed> findById(Long id) { return feedJpaRepository.findById(id); }

    @Transactional
    public void deleteById(Long id) { feedJpaRepository.deleteById(id); }

    /* ========= 커서 기반 목록 ========= */
    @Transactional(readOnly = true)
    public List<Feed> findFeedsByCursor(Long cursorId, Pageable pageable) {
        return query.selectFrom(feed)
            .where(cursorId == null ? null : feed.id.lt(cursorId))
            .orderBy(feed.id.desc())
            .limit(pageable.getPageSize())
            .fetch();
    }

    /* ========= 커서 + Member fetch join (N+1 방지) ========= */
    @Transactional(readOnly = true)
    public List<Feed> findFeedsByCursorWithMember(Long cursorId, Pageable pageable) {
        return query.selectFrom(feed)
            .join(feed.member, member).fetchJoin()
            .where(cursorId == null ? null : feed.id.lt(cursorId))
            .orderBy(feed.id.desc())
            .limit(pageable.getPageSize())
            .fetch();
    }

    /* ========= 상세 + Member fetch join ========= */
    @Transactional(readOnly = true)
    public Optional<Feed> findByIdWithMember(Long feedId) {
        Feed result = query.selectFrom(feed)
            .join(feed.member, member).fetchJoin()
            .where(feed.id.eq(feedId))
            .fetchOne();
        return Optional.ofNullable(result);
    }

    /* ========= 좋아요 배치 집계 (distinct member) ========= */
    @Transactional(readOnly = true)
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

    /* ========= 좋아요 단건 집계 ========= */
    @Transactional(readOnly = true)
    public long countDistinctMemberByFeedId(Long feedId) {
        Long cnt = query
            .select(feedLike.member.id.countDistinct())
            .from(feedLike)
            .where(feedLike.feed.id.eq(feedId))
            .fetchOne();
        return cnt == null ? 0L : cnt;
    }

    /* ========= 존재 여부 =========> 추후 개발을 위해 구현 */
    @Transactional(readOnly = true)
    public boolean existsByIdAndMemberId(Long feedId, Long memberId) {
        Integer one = query.selectOne()
            .from(feed)
            .where(feed.id.eq(feedId).and(feed.member.id.eq(memberId)))
            .fetchFirst();
        return one != null;
    }
}
