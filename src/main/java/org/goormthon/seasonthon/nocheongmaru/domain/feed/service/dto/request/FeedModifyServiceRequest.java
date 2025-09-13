package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record FeedModifyServiceRequest(
    
    Long memberId,
    Long feedId,
    String description,
    MultipartFile imageFile
    
) {
}
