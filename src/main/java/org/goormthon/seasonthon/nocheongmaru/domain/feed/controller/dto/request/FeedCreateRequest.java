package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request.FeedCreateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record FeedCreateRequest(
    
    @NotNull(message = "미션 ID는 필수입니다.")
    Long missionId,
    
    @NotBlank(message = "피드 설명은 필수입니다.")
    String description
    
) {
    
    public FeedCreateServiceRequest toServiceRequest(Long memberId, MultipartFile imageFile) {
        return FeedCreateServiceRequest.builder()
            .memberId(memberId)
            .missionId(missionId)
            .description(description)
            .imageFile(imageFile)
            .build();
    }
    
}
