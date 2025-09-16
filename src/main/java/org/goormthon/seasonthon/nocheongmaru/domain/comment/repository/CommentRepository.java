package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;


import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.CommentNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentRepository {
    
    private final CommentJpaRepository commentJpaRepository;
    
    public void save(Comment comment) {
        commentJpaRepository.save(comment);
    }
    
    public void deleteAllInBatch() {
        commentJpaRepository.deleteAllInBatch();
    }
    
    public Comment findById(Long id) {
        return commentJpaRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }
    
    public boolean existsByIdAndFeedId(Long commentId, Long id) {
        return commentJpaRepository.existsByIdAndFeedId(commentId, id);
    }
    
    public boolean existsByIdAndMemberId(Long commentId, Long memberId) {
        return commentJpaRepository.existsByIdAndMemberId(commentId, memberId);
    }
    
    public void deleteById(Long commentId) {
        commentJpaRepository.deleteById(commentId);
    }
    
    public List<CommentResponse> getCommentsByFeedId(Long feedId, Long memberId, Long lastCommentId) {
        return commentJpaRepository.getCommentsByFeedId(feedId, memberId, lastCommentId);
    }
    
    public void deleteByFeedId(Long feedId) {
        commentJpaRepository.deleteByFeedId(feedId);
    }
    
}
