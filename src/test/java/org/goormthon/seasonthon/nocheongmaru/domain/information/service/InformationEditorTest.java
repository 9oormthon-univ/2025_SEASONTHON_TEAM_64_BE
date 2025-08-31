package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.KakaoGeocodingProvider;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.IsNotInformationOwnerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class InformationEditorTest extends IntegrationTestSupport {
    
    @Autowired
    private InformationEditor informationEditor;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private InformationRepository informationRepository;
    
    @MockitoBean
    private KakaoGeocodingProvider kakaoGeocodingProvider;
    
    @AfterEach
    void tearDown() {
        informationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("정보나눔 피드를 수정한다.")
    @Test
    void modify() {
        // given
        Member member = createMember("nickname", "test@test.com");
        memberRepository.save(member);
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        GeocodingResponse geocodingResponse = GeocodingResponse.builder()
            .latitude(36.123456)
            .longitude(126.123456)
            .build();
        given(kakaoGeocodingProvider.getGeocodingByAddress("modify-address"))
            .willReturn(geocodingResponse);
        
        // when
        Long informationId = informationEditor.modify(
            member.getId(),
            information.getId(),
            "modified title",
            "modified description",
            "modify-address"
        );
        
        // then
        Information modifiedInformation = informationRepository.findById(informationId);
        assertThat(modifiedInformation)
            .extracting("title", "description", "address")
            .containsExactly("modified title", "modified description", "modify-address");
    }
    
    @DisplayName("정보나눔 피드 수정 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void modify_WithIsNotOwner() {
        // given
        Member member = createMember("nickname", "test1@test.com");
        Member otherMember = createMember("other-nickname", "test2@test.com");
        memberRepository.saveAll(List.of(member, otherMember));
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        // when & then
        assertThatThrownBy(() -> informationEditor.modify(otherMember.getId(), information.getId(), "modified title", "modified description", "modify-address"))
            .isInstanceOf(IsNotInformationOwnerException.class)
            .hasMessage("정보나눔 피드의 작성자가 아닙니다.");
    }
    
    @DisplayName("정보나눔 피드를 삭제한다.")
    @Test
    void delete() {
        // given
        Member member = createMember("nickname", "test@test.com");
        memberRepository.save(member);
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        // when
        informationEditor.delete(member.getId(), information.getId());
        
        // then
        assertThat(informationRepository.existsById(information.getId())).isFalse();
    }
    
    @DisplayName("정보나눔 피드 삭제 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void delete_WithIsNotOwner() {
        // given
        Member member = createMember("nickname", "test1@test.com");
        Member otherMember = createMember("other-nickname", "test2@test.com");
        memberRepository.saveAll(List.of(member, otherMember));
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        // when & then
        assertThatThrownBy(() -> informationEditor.delete(otherMember.getId(), information.getId()))
            .isInstanceOf(IsNotInformationOwnerException.class)
            .hasMessage("정보나눔 피드의 작성자가 아닙니다.");
    }
    
    private Member createMember(String nickname, String email) {
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .profileImageURL("profileImageURL")
            .role(Role.ROLE_USER)
            .build();
    }
    
    private Information createInformation(Member member) {
        return Information.builder()
            .member(member)
            .title("title")
            .description("description")
            .address("address")
            .category(Category.HOSPITAL_FACILITIES)
            .latitude(37.123456)
            .longitude(127.123456)
            .build();
    }
    
}