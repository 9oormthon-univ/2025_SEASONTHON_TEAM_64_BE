package org.goormthon.seasonthon.nocheongmaru.domain.image.repository;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ImageRepository {
    
    private final ImageJpaRepository imageJpaRepository;
    
    public void save(Image image) {
        imageJpaRepository.save(image);
    }
    
    public void saveAll(List<Image> images) {
        imageJpaRepository.saveAll(images);
    }
    
    public void deleteAllInBatch() {
        imageJpaRepository.deleteAllInBatch();
    }
    
    public List<Image> findAllByInformationId(Long informationId) {
        return imageJpaRepository.findAllByInformationId(informationId);
    }
    
    public void deleteAllByInformationId(Long informationId) {
        imageJpaRepository.deleteAllByInformationId(informationId);
    }
    
}
