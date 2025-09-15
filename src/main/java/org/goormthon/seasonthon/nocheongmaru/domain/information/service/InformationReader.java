package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.image.repository.ImageRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto.ImageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InformationReader {
    
    private final InformationRepository informationRepository;
    private final ImageRepository imageRepository;
    
    @Transactional(readOnly = true)
    public InformationDetailResponse getInformationDetail(Long informationId) {
        Information information = informationRepository.findById(informationId);
        List<ImageResponse> images = imageRepository.getImages(informationId);
        MemberDetailResponse writer = toMemberDetailResponse(information);
        
        return InformationDetailResponse.builder()
            .informationId(information.getId())
            .title(information.getTitle())
            .description(information.getDescription())
            .category(information.getCategory().toString())
            .address(information.getAddress())
            .latitude(information.getLatitude())
            .longitude(information.getLongitude())
            .createdAt(LocalDate.from(information.getCreatedAt()))
            .writer(writer)
            .images(images)
            .build();
    }
    
    @Transactional(readOnly = true)
    public List<InformationResponse> getInformationList(Long lastId, String category, Boolean sortByRecent) {
        return informationRepository.getInformationList(lastId, category, sortByRecent);
    }
    
    @Transactional(readOnly = true)
    public List<InformationResponse> getMyInformationList(Long memberId, Long lastId) {
        return informationRepository.getMyInformationList(memberId, lastId);
    }
    
    private MemberDetailResponse toMemberDetailResponse(Information information) {
        return MemberDetailResponse.builder()
            .memberId(information.getMember().getId())
            .nickname(information.getMember().getNickname())
            .email(information.getMember().getEmail())
            .profileImageUrl(information.getMember().getProfileImageURL())
            .role(information.getMember().getRole().toString())
            .mode(information.getMember().getMode().toString())
            .build();
    }
    
}
