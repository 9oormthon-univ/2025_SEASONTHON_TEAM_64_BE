package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.FeedNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.QFeed.feed;
import static org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.QFeedLike.feedLike;

@RequiredArgsConstructor
@Repository
public class FeedCustomRepositoryImpl implements FeedCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    private static final long PAGE_SIZE = 8L;
    
    @Override
    public List<FeedResponse> getFeeds(Long memberId, Long lastFeedId) {
        return queryFactory.select(constructor(FeedResponse.class,
                feed.id,
                feed.member.nickname,
                feed.member.profileImageURL,
                feed.description,
                feed.imageUrl,
                feed.member.id.eq(memberId).as("isMine"),
                feedLike.member.id.eq(memberId).as("isLiked"),
                feed.createdAt
            )).from(feed)
            .leftJoin(feedLike)
            .on(feedLike.feed.id.eq(feed.id).and(feedLike.member.id.eq(memberId)))
            .where(
                cursor(lastFeedId)
            )
            .orderBy(feed.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    @Override
    public FeedResponse getFeed(Long memberId, Long feedId) {
        FeedResponse feedResponse = queryFactory.select(constructor(FeedResponse.class,
                feed.id,
                feed.member.nickname,
                feed.member.profileImageURL,
                feed.description,
                feed.imageUrl,
                feed.member.id.eq(memberId).as("isMine"),
                feedLike.member.id.eq(memberId).as("isLiked"),
                feed.createdAt
            )).from(feed)
            .leftJoin(feedLike)
            .on(feedLike.feed.id.eq(feed.id).and(feedLike.member.id.eq(memberId)))
            .where(feed.id.eq(feedId))
            .fetchOne();
        
        if(feedResponse == null) {
            throw new FeedNotFoundException();
        }
        
        return feedResponse;
    }
    
    @Override
    public List<FeedResponse> getMyFeeds(Long memberId, Long lastFeedId) {
        return queryFactory.select(constructor(FeedResponse.class,
                feed.id,
                feed.member.nickname,
                feed.member.profileImageURL,
                feed.description,
                feed.imageUrl,
                feed.member.id.eq(memberId).as("isMine"),
                feedLike.member.id.eq(memberId).as("isLiked"),
                feed.createdAt
            )).from(feed)
            .leftJoin(feedLike)
            .on(feedLike.feed.id.eq(feed.id).and(feedLike.member.id.eq(memberId)))
            .where(
                cursor(lastFeedId),
                feed.member.id.eq(memberId)
            )
            .orderBy(feed.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    private BooleanExpression cursor(Long lastId) {
        return lastId == null ? null : feed.id.lt(lastId);
    }
    
}
