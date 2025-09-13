package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    private static final long PAGE_SIZE = 8L;
    
    @Override
    public List<CommentResponse> getCommentsByFeedId(Long feedId, Long memberId, Long lastCommentId) {
        return queryFactory.select(constructor(CommentResponse.class,
            comment.id.as("commentId"),
            comment.feed.id.as("feedId"),
            comment.member.nickname.as("nickname"),
            comment.member.profileImageURL.as("imageUrl"),
            comment.description.as("description"),
            comment.member.id.eq(memberId).as("isMine"),
            comment.createdAt.as("createdAt")
            ))
            .from(comment)
            .where(
                comment.feed.id.eq(feedId),
                cursor(lastCommentId)
            )
            .orderBy(comment.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    private BooleanExpression cursor(Long lastId) {
        return lastId == null ? null : comment.id.lt(lastId);
    }
    
}
