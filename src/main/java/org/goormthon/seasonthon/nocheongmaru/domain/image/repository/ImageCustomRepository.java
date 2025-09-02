package org.goormthon.seasonthon.nocheongmaru.domain.image.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto.ImageResponse;

import java.util.List;

public interface ImageCustomRepository {
    
    List<ImageResponse> getImages(Long informationId);
    
}
