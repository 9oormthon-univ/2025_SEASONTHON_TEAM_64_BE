package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record FeedCreateServiceRequest(
    
    Long missionId,
    Long memberId,
    String description,
    MultipartFile imageFile
    
) {
}
