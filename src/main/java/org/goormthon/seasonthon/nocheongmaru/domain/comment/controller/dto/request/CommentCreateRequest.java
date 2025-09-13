package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.request.CommentCreateServiceRequest;

@Builder
public record CommentCreateRequest(
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    String description
    
) {
    
    public CommentCreateServiceRequest toServiceRequest(Long memberId, Long feedId) {
        return CommentCreateServiceRequest.builder()
            .memberId(memberId)
            .feedId(feedId)
            .description(description)
            .build();
    }
}
