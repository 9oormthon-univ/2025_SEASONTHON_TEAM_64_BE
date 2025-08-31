package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.ImageManager;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.IsNotInformationOwnerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Component
public class InformationEditor {
    
    private final KakaoGeocodingProvider kakaoGeocodingProvider;
    private final InformationRepository informationRepository;
    private final ImageManager imageManager;
    
    @Transactional
    public Long modify(Long memberId, Long informationId, String title, String description, String address, List<MultipartFile> images) {
        Information information = informationRepository.findById(informationId);
        validateInformationOwnership(memberId, information.getId());
        
        GeocodingResponse geocodingResponse = kakaoGeocodingProvider.getGeocodingByAddress(address);
        information.modifyInformation(title, description, address, geocodingResponse.latitude(), geocodingResponse.longitude());
        imageManager.replaceImages(informationId, information, images);
        
        return information.getId();
    }
    
    @Transactional
    public void delete(Long memberId, Long informationId) {
        validateInformationOwnership(memberId, informationId);
        imageManager.deleteAllByInformationId(informationId);
        informationRepository.deleteById(informationId);
    }
    
    private void validateInformationOwnership(Long memberId, Long informationId) {
        if (!informationRepository.existsByIdAndMemberId(informationId, memberId)) {
            throw new IsNotInformationOwnerException();
        }
    }
    
}
