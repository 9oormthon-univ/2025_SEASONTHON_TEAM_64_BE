package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedLikeRepository {
    
    private final FeedLikeJpaRepository feedLikeJpaRepository;

    /* ========= 기본 CRUD 위임 ========= */
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
    
}
