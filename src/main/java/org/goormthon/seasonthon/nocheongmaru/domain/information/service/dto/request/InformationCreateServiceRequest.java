package org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;

@Builder
public record InformationCreateServiceRequest(
    
    Long memberId,
    String title,
    String description,
    String address,
    Category category
    
) {
}
