package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.InformationImageManager;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.FilteringProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Component
public class InformationGenerator {
    
    private final InformationRepository informationRepository;
    private final InformationImageManager informationImageManager;
    private final KakaoGeocodingProvider kakaoGeocodingProvider;
    private final FilteringProvider filteringProvider;
    
    @Transactional
    public Long generate(Member member, String title, String description, String address, Category category, List<MultipartFile> images) {
        filteringProvider.validateViolent(description);
        GeocodingResponse geo = kakaoGeocodingProvider.getGeocodingByAddress(address);
        
        Information information = createInformation(member, title, description, address, category, geo);
        informationRepository.save(information);
        
        informationImageManager.saveImages(information, images);
        
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
    
}