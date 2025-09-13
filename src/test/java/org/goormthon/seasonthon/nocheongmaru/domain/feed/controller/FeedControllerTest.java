package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    
    @TestMember
    @DisplayName("오늘의 시선 피드를 수정한다.")
    @Test
    void modifyFeed() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 10L;
        MockMultipartFile imageFile = createMockMultipartFile();
        var request = FeedModifyRequest.builder()
            .description("새 설명")
            .build();
        
        // 컨트롤러는 request.toServiceRequest(memberId, feedId, imageFile)를 호출하므로 동일하게 스텁
        willDoNothing().given(feedService).modifyFeed(request.toServiceRequest(memberId, feedId, imageFile));
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + feedId)
                    .file(imageFile)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @TestMember
    @DisplayName("오늘의 시선 피드 수정 시, 설명은 필수이다.")
    @Test
    void modifyFeed_WithNonDescription() throws Exception {
        // given
        Long feedId = 10L;
        var request = FeedModifyRequest.builder()
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + feedId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("피드 설명은 필수입니다."));
    }
    
    @TestMember
    @DisplayName("오늘의 시선 피드를 삭제한다.")
    @Test
    void deleteFeed() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 99L;
        willDoNothing().given(feedService).deleteFeed(memberId, feedId);
        
        // expected
        mockMvc.perform(
                delete(BASE_URL + "/" + feedId)
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @TestMember
    @DisplayName("오늘의 시선 피드를 상세 조회한다.")
    @Test
    void getFeed() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 123L;
        FeedResponse response = FeedResponse.builder()
            .feedId(feedId)
            .nickname("tester")
            .profileImageUrl("http://example.com/profile.jpg")
            .description("상세 설명")
            .imageUrl("http://example.com/image.png")
            .isMine(true)
            .isLiked(true)
            .createdAt(LocalDateTime.now())
            .build();
        given(feedService.getFeed(memberId, feedId)).willReturn(response);
        
        // expected
        mockMvc.perform(
                get(BASE_URL + "/" + feedId)
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.feedId").value(feedId))
            .andExpect(jsonPath("$.nickname").value("tester"))
            .andExpect(jsonPath("$.isMine").value(true))
            .andExpect(jsonPath("$.isLiked").value(true));
    }
    
    @TestMember
    @DisplayName("lastFeedId가 null일 때, 오늘의 시선 피드 목록을 조회한다.")
    @Test
    void getFeeds_WithoutLastFeedId() throws Exception {
        // given
        Long memberId = 1L;
        List<FeedResponse> responses = List.of(
            FeedResponse.builder()
                .feedId(10L)
                .nickname("user1")
                .profileImageUrl("http://example.com/p1.jpg")
                .description("desc1")
                .imageUrl("http://example.com/i1.png")
                .isMine(false)
                .isLiked(false)
                .createdAt(LocalDateTime.now())
                .build(),
            FeedResponse.builder()
                .feedId(9L)
                .nickname("user2")
                .profileImageUrl("http://example.com/p2.jpg")
                .description("desc2")
                .imageUrl("http://example.com/i2.png")
                .isMine(true)
                .isLiked(true)
                .createdAt(LocalDateTime.now())
                .build()
        );
        given(feedService.getFeeds(memberId, null)).willReturn(responses);
        
        // expected
        mockMvc.perform(
                get(BASE_URL)
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].feedId").value(10))
            .andExpect(jsonPath("$[1].isLiked").value(true));
    }
    
    @TestMember
    @DisplayName("lastFeedId가 null이 아닐 때, 오늘의 시선 피드 목록을 조회한다.")
    @Test
    void getFeeds_WithLastFeedId() throws Exception {
        // given
        Long memberId = 1L;
        Long lastFeedId = 100L;
        List<FeedResponse> responses = List.of(
            FeedResponse.builder()
                .feedId(99L)
                .nickname("user1")
                .profileImageUrl("http://example.com/p1.jpg")
                .description("desc1")
                .imageUrl("http://example.com/i1.png")
                .isMine(false)
                .isLiked(false)
                .createdAt(LocalDateTime.now())
                .build()
        );
        given(feedService.getFeeds(memberId, lastFeedId)).willReturn(responses);
        
        // expected
        mockMvc.perform(
                get(BASE_URL)
                    .param("lastFeedId", String.valueOf(lastFeedId))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].feedId").value(99));
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
