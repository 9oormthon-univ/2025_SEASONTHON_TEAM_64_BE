package org.goormthon.seasonthon.nocheongmaru.domain.information.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InformationControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/informations";
    
    @TestMember
    @DisplayName("정보나눔 피드를 생성한다.")
    @Test
    void createInformation() throws Exception {
        // given
        Long memberId = 1L;
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        given(informationService.generateInformation(request.toServiceRequest(memberId)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 제목은 필수이다.")
    @Test
    void createInformation_WithNonTitle() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.title").value("제목은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 설명은 필수이다.")
    @Test
    void createInformation_WithNonDescription() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 설명은 최대 200자까지 입력 가능하다.")
    @Test
    void createInformation_WithTooLongDescription() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("d".repeat(201))
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 최대 200자까지 입력 가능합니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 주소는 필수이다.")
    @Test
    void createInformation_WithNonAddress() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.address").value("주소는 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 카테고리는 필수이다.")
    @Test
    void createInformation_WithNonCategory() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 카테고리는 정확한 값이어야 한다.")
    @Test
    void createInformation_WithInaccurateCategory() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("INVALID_CATEGORY")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드를 수정한다.")
    @Test
    void modifyInformation() throws Exception {
        // given
        Long memberId = 1L;
        Long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        given(informationService.modifyInformation(request.toServiceRequest(memberId, informationId)))
            .willReturn(informationId);
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 제목은 필수이다.")
    @Test
    void modifyInformation_WithNonTitle() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.title").value("제목은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 설명은 필수이다.")
    @Test
    void modifyInformation_WithNonDescription() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 설명은 최대 200자까지 입력 가능하다.")
    @Test
    void modifyInformation_WithTooLongDescription() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("d".repeat(201))
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 최대 200자까지 입력 가능합니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 주소는 필수이다.")
    @Test
    void modifyInformation_WithNonAddress() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.address").value("주소는 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 카테고리는 필수이다.")
    @Test
    void modifyInformation_WithNonCategory() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 카테고리는 정확한 값이어야 한다.")
    @Test
    void modifyInformation_WithInaccurateCategory() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("INVALID_CATEGORY")
            .build();
        
        // expected
        mockMvc.perform(
                put(BASE_URL + "/" + informationId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
}