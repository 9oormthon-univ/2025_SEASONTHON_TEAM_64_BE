package org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;

@Builder
public record InformationModifyServiceRequest (
    
    Long memberId,
    Long informationId,
    String title,
    String description,
    String address,
    Category category
    
) {
}
