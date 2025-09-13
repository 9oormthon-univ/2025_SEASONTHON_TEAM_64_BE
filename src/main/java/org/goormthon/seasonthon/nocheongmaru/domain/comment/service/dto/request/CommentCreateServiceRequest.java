package org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateServiceRequest(
    
    Long memberId,
    Long feedId,
    String description
    
) {
}
