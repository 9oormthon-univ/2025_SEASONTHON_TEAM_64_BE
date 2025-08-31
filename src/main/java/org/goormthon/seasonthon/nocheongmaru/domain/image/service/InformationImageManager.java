package org.goormthon.seasonthon.nocheongmaru.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.goormthon.seasonthon.nocheongmaru.domain.image.repository.ImageRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InformationImageManager {

    private final ImageRepository imageRepository;
    private final S3StorageUtil s3StorageUtil;
    
    @Transactional
    public void saveImages(Information information, List<MultipartFile> images) {
        List<Image> imageList = buildImages(information, images);
        if (!imageList.isEmpty()) {
            imageRepository.saveAll(imageList);
        }
    }

    @Transactional
    public void replaceImages(Long informationId, Information information, List<MultipartFile> images) {
        List<Image> existingImages = imageRepository.findAllByInformationId(informationId);
        if (!existingImages.isEmpty()) {
            existingImages.forEach(image -> s3StorageUtil.deleteFileFromS3(image.getImageUrl()));
            imageRepository.deleteAllByInformationId(informationId);
        }
        saveImages(information, images);
    }
    
    @Transactional
    public void deleteAllByInformationId(Long informationId) {
        List<Image> existingImages = imageRepository.findAllByInformationId(informationId);
        if (existingImages.isEmpty()) {
            return;
        }
        existingImages.forEach(image -> s3StorageUtil.deleteFileFromS3(image.getImageUrl()));
        imageRepository.deleteAllByInformationId(informationId);
    }

    public List<Image> buildImages(Information information, List<MultipartFile> images) {
        return Optional.ofNullable(images)
            .orElse(Collections.emptyList())
            .stream()
            .filter(file -> file != null && !file.isEmpty())
            .map(file -> createImage(information, file))
            .toList();
    }

    private Image createImage(Information information, MultipartFile imageFile) {
        String fileName = s3StorageUtil.uploadFileToS3(imageFile);
        return Image.builder()
            .imageUrl(fileName)
            .information(information)
            .build();
    }
    
}
