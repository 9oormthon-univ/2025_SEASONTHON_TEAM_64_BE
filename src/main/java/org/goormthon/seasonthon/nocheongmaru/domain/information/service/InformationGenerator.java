package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.goormthon.seasonthon.nocheongmaru.domain.image.repository.ImageRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InformationGenerator {
    
    private final InformationRepository informationRepository;
    private final ImageRepository imageRepository;
    private final S3StorageUtil s3StorageUtil;
    private final KakaoGeocodingProvider kakaoGeocodingProvider;
    
    @Transactional
    public Long generate(Member member, String title, String description, String address, Category category, List<MultipartFile> images) {
        GeocodingResponse geo = kakaoGeocodingProvider.getGeocodingByAddress(address);
        
        Information information = createInformation(member, title, description, address, category, geo);
        informationRepository.save(information);
        
        List<Image> imageList = Optional.ofNullable(images)
            .orElse(Collections.emptyList())
            .stream()
            .filter(file -> file != null && !file.isEmpty())
            .map(file -> createImage(information, file))
            .toList();
        
        if (!imageList.isEmpty()) {
            imageRepository.saveAll(imageList);
        }
        
        return information.getId();
    }
    
    private Information createInformation(Member member, String title, String description, String address, Category category, GeocodingResponse geo) {
        return Information.builder()
            .member(member)
            .title(title)
            .description(description)
            .address(address)
            .latitude(geo.latitude())
            .longitude(geo.longitude())
            .category(category)
            .build();
    }
    
    private Image createImage(Information information, MultipartFile imageFile) {
        String fileName = s3StorageUtil.uploadFileToS3(imageFile);
        return Image.builder()
            .imageUrl(fileName)
            .information(information)
            .build();
    }
    
}