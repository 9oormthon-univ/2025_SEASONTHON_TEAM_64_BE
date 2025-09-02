package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import static org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.QFeedLike.*;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class FeedLikeRepository {
    
    private final FeedLikeJpaRepository feedLikeJpaRepository;
    private final JPAQueryFactory query;
    private final EntityManager em;

    public FeedLike save(FeedLike entity) { return feedLikeJpaRepository.save(entity); }

    public Optional<FeedLike> findById(Long id) { return feedLikeJpaRepository.findById(id); }

    public void deleteById(Long id) { feedLikeJpaRepository.deleteById(id); }

    public boolean existsByFeed_IdAndMember_Id(Long feedId, Long memberId) {
        return feedLikeJpaRepository.existsByFeed_IdAndMember_Id(feedId, memberId);
    }

    public long deleteByFeed_IdAndMember_Id(Long feedId, Long memberId) {
        return feedLikeJpaRepository.deleteByFeed_IdAndMember_Id(feedId, memberId);
    }

    public long countByFeed_Id(Long feedId) {
        return feedLikeJpaRepository.countByFeed_Id(feedId);
    }

    public long deleteAllByFeedId(Long feedId) {
        long deleted = query
            .delete(feedLike)
            .where(feedLike.feed.id.eq(feedId))
            .execute();
        em.clear();
        return deleted;
    }
    
}
