package org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request;

import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record InformationCreateServiceRequest(
    
    Long memberId,
    String title,
    String description,
    String address,
    Category category,
    List<MultipartFile> images
    
) {
}
