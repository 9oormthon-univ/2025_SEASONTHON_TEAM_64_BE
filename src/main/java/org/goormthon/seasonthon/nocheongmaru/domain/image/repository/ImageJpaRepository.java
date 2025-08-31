package org.goormthon.seasonthon.nocheongmaru.domain.image.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    
    List<Image> findAllByInformationId(Long informationId);
    
}
