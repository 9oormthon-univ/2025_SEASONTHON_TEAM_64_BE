package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;

import java.util.List;

public interface CommentCustomRepository {
    
    List<CommentResponse> getCommentsByFeedId(Long feedId, Long memberId, Long lastCommentId);
    
}
