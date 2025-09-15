package org.goormthon.seasonthon.nocheongmaru.domain.notification.event;

import lombok.Builder;

@Builder
public record FeedLikedEvent(

    Long recipientId,

    String likerNickname,

    Long feedId

) {
}

