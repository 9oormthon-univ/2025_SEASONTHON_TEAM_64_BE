package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    
}