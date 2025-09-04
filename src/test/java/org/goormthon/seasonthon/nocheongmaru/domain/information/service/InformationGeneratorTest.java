package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.InformationImageManager;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class InformationGeneratorTest extends IntegrationTestSupport {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private InformationRepository informationRepository;
    
    @Autowired
    private InformationGenerator informationGenerator;
    
    @MockitoBean
    private KakaoGeocodingProvider kakaoGeocodingProvider;
    
    @MockitoBean
    private InformationImageManager informationImageManager;
    
    @AfterEach
    void tearDown() {
        informationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("정보나눔 피드를 생성한다.")
    @Test
    void generate() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        GeocodingResponse geocodingResponse = GeocodingResponse.builder()
            .latitude(37.123456)
            .longitude(127.123456)
            .build();
        given(kakaoGeocodingProvider.getGeocodingByAddress("address"))
            .willReturn(geocodingResponse);
        
        MockMultipartFile image1 = createMockFile("image1", "image1");
        MockMultipartFile image2 = createMockFile("image2", "image2");
        
        
        // when
        Long informationId = informationGenerator.generate(
            member,
            "title",
            "description",
            "address",
            Category.HOSPITAL_FACILITIES,
            List.of(image1, image2)
        );
        
        // then
        Information information = informationRepository.findById(informationId);
        assertThat(information)
            .extracting("id", "title", "description", "address", "latitude", "longitude", "category")
            .containsExactly(information.getId(), "title", "description", "address", 37.123456, 127.123456, Category.HOSPITAL_FACILITIES);
        verify(informationImageManager).saveImages(any(Information.class), eq(List.of(image1, image2)));
    }
    
    @DisplayName("정보나눔 피드 생성 시 이미지는 없어도 된다.")
    @Test
    void generate_WithoutImages() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        GeocodingResponse geocodingResponse = GeocodingResponse.builder()
            .latitude(37.123456)
            .longitude(127.123456)
            .build();
        given(kakaoGeocodingProvider.getGeocodingByAddress("address"))
            .willReturn(geocodingResponse);
        
        // when
        Long informationId = informationGenerator.generate(
            member,
            "title",
            "description",
            "address",
            Category.HOSPITAL_FACILITIES,
            null
        );
        
        // then
        Information information = informationRepository.findById(informationId);
        assertThat(information)
            .extracting("id", "title", "description", "address", "latitude", "longitude", "category")
            .containsExactly(information.getId(), "title", "description", "address", 37.123456, 127.123456, Category.HOSPITAL_FACILITIES);
        verify(informationImageManager).saveImages(any(Information.class), isNull());
    }
    
    private Member createMember() {
        return Member.builder()
            .nickname("nickname")
            .email("test@test.com")
            .profileImageURL("profileImageURL")
            .role(Role.ROLE_USER)
            .build();
    }
    
    private MockMultipartFile createMockFile(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename + ".png", MediaType.IMAGE_PNG_VALUE, name.getBytes());
    }
    
}