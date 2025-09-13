package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CommentReader {
    
    private final CommentRepository commentRepository;
    
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByFeedId(Long feedId, Long memberId, Long lastCommentId) {
        return commentRepository.getCommentsByFeedId(feedId, memberId, lastCommentId);
    }
    
}
