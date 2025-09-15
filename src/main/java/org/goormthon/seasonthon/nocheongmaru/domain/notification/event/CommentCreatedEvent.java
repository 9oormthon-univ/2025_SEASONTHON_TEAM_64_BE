package org.goormthon.seasonthon.nocheongmaru.domain.notification.event;

import lombok.Builder;

@Builder
public record CommentCreatedEvent(
    
    Long recipientId,
    
    String commenterNickname,
    
    Long feedId
    
) {
}

