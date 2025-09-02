package org.goormthon.seasonthon.nocheongmaru.domain.image.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto.ImageResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.image.entity.QImage.image;

@RequiredArgsConstructor
@Repository
public class ImageCustomRepositoryImpl implements ImageCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<ImageResponse> getImages(Long informationId) {
        return queryFactory.select(constructor(ImageResponse.class,
                image.id.as("imageId"),
                image.imageUrl
            )).from(image)
            .where(image.information.id.eq(informationId))
            .fetch();
    }
    
}
