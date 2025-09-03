package org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed;


import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class FeedRepository {

    private final FeedJpaRepository feedJpaRepository;

    public Feed save(Feed entity) {
        return feedJpaRepository.save(entity);
    }

    public Feed findById(Long id) {
        return feedJpaRepository.findById(id)
            .orElseThrow(FeedNotFoundException::new);
    }

    public void deleteById(Long id) {
        feedJpaRepository.deleteById(id);
    }

    // 커서 페이징 + member fetch
    public List<Feed> findFeedsByCursorWithMember(Long cursorId, Pageable pageable) {
        if (cursorId == null) {
            return feedJpaRepository.findAllByOrderByIdDesc(pageable);
        }
        return feedJpaRepository.findByIdLessThanOrderByIdDesc(cursorId, pageable);
    }

    // 단건 + member fetch
    public Optional<Feed> findByIdWithMember(Long feedId) {
        return feedJpaRepository.findOneWithMemberById(feedId);
    }

    // 좋아요 사용자 수 (배치)
    public List<FeedIdCount> countDistinctMemberByFeedIds(List<Long> feedIds) {
        if (feedIds == null || feedIds.isEmpty()) return List.of();
        return feedJpaRepository.countDistinctLikerGroupByFeedIds(feedIds);
    }

    // 좋아요 사용자 수 (단건)
    public long countDistinctMemberByFeedId(Long feedId) {
        return feedJpaRepository.countDistinctLikerByFeedId(feedId);
    }

    public boolean existsByIdAndMemberId(Long feedId, Long memberId) {
        return feedJpaRepository.existsByIdAndMember_Id(feedId, memberId);
    }

    public void delete(Feed feed) {
        feedJpaRepository.delete(feed);
    }

    public boolean existsByMemberAndMission(Long memberId, Long missionId) {
        return feedJpaRepository.existsByMember_IdAndMission_Id(memberId, missionId);
    }

    public void deleteByIdAndMember(Long feedId, Long memberId) {
        if (!feedJpaRepository.existsById(feedId)) {
            throw new EntityNotFoundException();
        }
        feedJpaRepository.deleteByIdAndMember_Id(feedId, memberId);
    }

    public boolean existsByMember_IdAndMission_Id(Long memberId, Long missionId) {
        return feedJpaRepository.existsByMember_IdAndMission_Id(memberId, missionId);
    }
}
