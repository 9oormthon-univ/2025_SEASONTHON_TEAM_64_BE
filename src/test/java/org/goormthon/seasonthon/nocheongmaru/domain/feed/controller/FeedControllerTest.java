package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/feeds";
    
    @TestMember
    @DisplayName("오늘의 시선 피드를 생성한다.")
    @Test
    void generateFeed() throws Exception {
        // given
        Long memberId = 1L;
        MockMultipartFile imageFile = createMockMultipartFile();
        var request = FeedCreateRequest.builder()
            .missionId(1L)
            .description("피드 설명")
            .build();
        
        given(feedService.generateFeed(request.toServiceRequest(memberId, imageFile)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(imageFile)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("오늘의 시선 피드 생성 시, 미션 ID는 필수이다.")
    @Test
    void generateFeed_WithNonMissionId() throws Exception {
        // given
        var request = FeedCreateRequest.builder()
            .description("피드 설명")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.missionId").value("미션 ID는 필수입니다."));
    }
    
    @TestMember
    @DisplayName("오늘의 시선 피드 생성 시, 설명은 필수이다.")
    @Test
    void generateFeed_WithNonDescription() throws Exception {
        // given
        var request = FeedCreateRequest.builder()
            .missionId(1L)
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("피드 설명은 필수입니다."));
    }
    
    private MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
            "imageFile",
            "test-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
    }
    
}

