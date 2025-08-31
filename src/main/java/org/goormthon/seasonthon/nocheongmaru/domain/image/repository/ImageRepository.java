package org.goormthon.seasonthon.nocheongmaru.domain.image.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ImageRepository {
    
    private final ImageJpaRepository imageJpaRepository;
    
}
