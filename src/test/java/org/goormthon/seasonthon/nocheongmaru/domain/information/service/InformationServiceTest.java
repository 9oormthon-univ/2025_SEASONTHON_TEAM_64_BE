package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request.InformationCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request.InformationModifyServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class InformationServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private InformationRepository informationRepository;
    
    @Autowired
    private InformationService informationService;
    
    @MockitoBean
    private InformationGenerator informationGenerator;
    
    @MockitoBean
    private InformationEditor informationEditor;
    
    @AfterEach
    void tearDown() {
        informationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("정보나눔 피드를 생성한다.")
    @Test
    void generateInformation() {
        // given
        Long informationId = 1L;
        
        Member member = createMember();
        memberRepository.save(member);
        
        var request = InformationCreateServiceRequest.builder()
            .memberId(member.getId())
            .title("title")
            .description("description")
            .address("address")
            .category(Category.HOSPITAL_FACILITIES)
            .build();
        
        given(informationGenerator.generate(any(), anyString(), anyString(), anyString(), any(), any()))
            .willReturn(informationId);
        
        // when
        Long createdInformationId = informationService.generateInformation(request);
        
        // then
        assertThat(createdInformationId).isEqualTo(informationId);
    }
    
    @DisplayName("정보나눔 피드를 수정한다.")
    @Test
    void modifyInformation() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        var request = InformationModifyServiceRequest.builder()
            .informationId(information.getId())
            .memberId(member.getId())
            .title("modified title")
            .description("modified description")
            .address("modified address")
            .category(Category.HOSPITAL_FACILITIES)
            .build();
        
        given(informationEditor.modify(any(), any(), anyString(), anyString(), anyString()))
            .willReturn(information.getId());
        
        // when
        Long modifiedInformationId = informationService.modifyInformation(request);
        
        // then
        assertThat(modifiedInformationId).isEqualTo(information.getId());
    }
    
    @DisplayName("정보나눔 피드를 삭제한다.")
    @Test
    void deleteInformation() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        // when
        informationService.deleteInformation(member.getId(), information.getId());
        
        // then
        verify(informationEditor).delete(any(), any());
    }
    
    private Member createMember() {
        return Member.builder()
            .nickname("nickname")
            .email("test@test.com")
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
            .latitude(136.123456)
            .longitude(356.123456)
            .category(Category.HOSPITAL_FACILITIES)
            .build();
    }
    
}