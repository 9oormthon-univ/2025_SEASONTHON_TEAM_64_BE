package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FeedResponse(
    
    Long feedId,
    String nickname,
    String profileImageUrl,
    String description,
    String imageUrl,
    Boolean isMine,
    Boolean isLiked,
    LocalDateTime createdAt
    
) {
}
