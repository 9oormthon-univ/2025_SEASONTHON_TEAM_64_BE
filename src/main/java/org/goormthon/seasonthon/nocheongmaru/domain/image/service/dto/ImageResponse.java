package org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto;

import lombok.Builder;

@Builder
public record ImageResponse(
    
    Long imageId,
    String imageUrl
    
) {
}
