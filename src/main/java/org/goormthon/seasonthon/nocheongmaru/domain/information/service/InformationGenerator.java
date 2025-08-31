package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class InformationGenerator {
    
    private final KakaoGeocodingProvider kakaoGeocodingProvider;
    private final InformationRepository informationRepository;
    
    @Transactional
    public Long generate(Member member, String title, String description, String address, Category category) {
        
        GeocodingResponse geocodingResponse = kakaoGeocodingProvider.getGeocodingByAddress(address);
        
        Information information = Information.builder()
            .member(member)
            .title(title)
            .description(description)
            .address(address)
            .latitude(geocodingResponse.latitude())
            .longitude(geocodingResponse.longitude())
            .category(category)
            .build();
        
        informationRepository.save(information);
        
        return information.getId();
    }
    
}
