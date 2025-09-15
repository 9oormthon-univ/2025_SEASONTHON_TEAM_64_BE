package org.goormthon.seasonthon.nocheongmaru.domain.notification.event;

import lombok.Builder;

import java.util.List;

@Builder
public record MissionAssignedEvent(
    
    List<Long> recipientIds
    
) {
}

