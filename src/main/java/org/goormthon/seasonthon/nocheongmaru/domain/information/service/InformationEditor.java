package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.IsNotInformationOwnerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class InformationEditor {
    
    private final KakaoGeocodingProvider kakaoGeocodingProvider;
    private final InformationRepository informationRepository;
    
    @Transactional
    public Long modify(Long memberId, Long informationId, String title, String description, String address) {
        Information information = informationRepository.findById(informationId);
        validateInformationOwnership(memberId, information.getId());
        
        GeocodingResponse geocodingResponse = kakaoGeocodingProvider.getGeocodingByAddress(address);
        information.modifyInformation(title, description, address, geocodingResponse.latitude(), geocodingResponse.longitude());
        
        return information.getId();
    }
    
    @Transactional
    public void delete(Long memberId, Long informationId) {
        validateInformationOwnership(memberId, informationId);
        informationRepository.deleteById(informationId);
    }
    
    private void validateInformationOwnership(Long memberId, Long informationId) {
        if (!informationRepository.existsByIdAndMemberId(informationId, memberId)) {
            throw new IsNotInformationOwnerException();
        }
    }
    
}
