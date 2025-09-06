package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;


import java.util.List;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.CommentNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class CommentRepository {

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
        if (cursorId == null) {
            return commentJpaRepository.findByFeedIdOrderByIdDescWithMember(feedId, pageable);
        }
        return commentJpaRepository.findByFeedIdAndIdLessThanOrderByIdDescWithMember(feedId, cursorId, pageable);
    }

    public boolean existsByIdAndMemberId(Long commentId, Long memberId) {
        return commentJpaRepository.existsByIdAndMemberId(commentId, memberId);
    }

    public List<FeedIdCount> countByFeedIds(List<Long> feedIds) {
        if (feedIds == null || feedIds.isEmpty()) return List.of();
        return commentJpaRepository.countGroupByFeedIds(feedIds);
    }

    public long countByFeedId(Long feedId) {
        return commentJpaRepository.countByFeedId(feedId);
    }

    public long deleteAllByFeedId(Long feedId) {
        return commentJpaRepository.deleteByFeedId(feedId);
    }

}
