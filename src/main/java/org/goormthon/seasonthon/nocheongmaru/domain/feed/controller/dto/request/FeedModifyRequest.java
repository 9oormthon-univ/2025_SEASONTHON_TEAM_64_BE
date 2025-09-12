package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request.FeedModifyServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record FeedModifyRequest(
    
    @NotBlank(message = "피드 설명은 필수입니다.")
    String description
    
) {
    
    public FeedModifyServiceRequest toServiceRequest(Long feedId, Long memberId, MultipartFile imageFile) {
        return FeedModifyServiceRequest.builder()
            .feedId(feedId)
            .memberId(memberId)
            .description(description)
            .imageFile(imageFile)
            .build();
    }
    
}
