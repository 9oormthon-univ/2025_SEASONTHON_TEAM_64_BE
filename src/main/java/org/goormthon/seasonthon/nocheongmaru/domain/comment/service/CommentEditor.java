package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.IsNotCommentInFeedException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.IsNotCommentOwnerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CommentEditor {
    
    private final CommentRepository commentRepository;
    
    @Transactional
    public void deleteComment(Long commentId, Long memberId, Long feedId) {
        validateComment(commentId, memberId, feedId);
        commentRepository.deleteById(commentId);
    }
    
    private void validateComment(Long commentId, Long memberId, Long feedId) {
        if(!commentRepository.existsByIdAndFeedId(commentId, feedId)) {
            throw new IsNotCommentInFeedException();
        }
        
        if(!commentRepository.existsByIdAndMemberId(commentId, memberId)) {
            throw new IsNotCommentOwnerException();
        }
    }
    
}
