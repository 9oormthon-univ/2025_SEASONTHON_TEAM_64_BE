package org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(

    Long commentId,
    Long feedId,
    String nickname,
    String imageUrl,
    String description,
    boolean isMine,
    LocalDateTime createdAt
    
) {
}
