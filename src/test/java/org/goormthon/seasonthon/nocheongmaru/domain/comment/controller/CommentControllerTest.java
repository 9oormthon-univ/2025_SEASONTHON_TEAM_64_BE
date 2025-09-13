package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/feeds";
    
    @TestMember
    @DisplayName("댓글을 생성한다.")
    @Test
    void generateComment() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 10L;
        var request = CommentCreateRequest.builder()
            .description("댓글 내용")
            .build();
        
        given(commentService.generateComment(request.toServiceRequest(memberId, feedId)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/" + feedId + "/comments")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("댓글 생성 시, 내용은 필수이다.")
    @Test
    void generateComment_WithNonDescription() throws Exception {
        // given
        Long feedId = 10L;
        var request = CommentCreateRequest.builder()
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/" + feedId + "/comments")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("댓글 내용은 필수입니다."));
    }
    
    @TestMember
    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 10L;
        Long commentId = 100L;
        
        // expected
        mockMvc.perform(
                delete(BASE_URL + "/" + feedId + "/comments/" + commentId)
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }
    
    @TestMember
    @DisplayName("피드의 댓글 목록을 조회한다.")
    @Test
    void getCommentsByFeedId() throws Exception {
        // given
        Long memberId = 1L;
        Long feedId = 10L;
        Long lastCommentId = 100L;
        List<CommentResponse> responses = List.of(
            CommentResponse.builder()
                .commentId(99L)
                .feedId(feedId)
                .nickname("user1")
                .imageUrl("http://example.com/p1.jpg")
                .description("c1")
                .isMine(true)
                .createdAt(LocalDateTime.now())
                .build(),
            CommentResponse.builder()
                .commentId(98L)
                .feedId(feedId)
                .nickname("user2")
                .imageUrl("http://example.com/p2.jpg")
                .description("c2")
                .isMine(false)
                .createdAt(LocalDateTime.now())
                .build()
        );
        given(commentService.getCommentsByFeedId(feedId, memberId, lastCommentId)).willReturn(responses);
        
        // expected
        mockMvc.perform(
                get(BASE_URL + "/" + feedId + "/comments")
                    .param("lastCommentId", String.valueOf(lastCommentId))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].commentId").value(99))
            .andExpect(jsonPath("$[0].nickname").value("user1"))
            .andExpect(jsonPath("$[0].isMine").value(true))
            .andExpect(jsonPath("$[1].commentId").value(98));
    }
    
    @TestMember
    @DisplayName("댓글 목록 조회 시 lastCommentId는 필수이다.")
    @Test
    void getCommentsByFeedId_MissingLastCommentId() throws Exception {
        // given
        Long feedId = 10L;
        
        // expected
        mockMvc.perform(
                get(BASE_URL + "/" + feedId + "/comments")
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("lastCommentId가 누락되었습니다."));
    }

}