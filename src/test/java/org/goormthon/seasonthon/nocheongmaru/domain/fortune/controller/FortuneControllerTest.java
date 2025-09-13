package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.request.FortuneCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FortuneControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/fortunes";
    
    @TestMember
    @DisplayName("오늘의 포춘쿠키를 생성한다.")
    @Test
    void generateFortune() throws Exception {
        // given
        Long memberId = 1L;
        String description = "오늘은 좋은 일이 생길 거예요!";
        
        var request = FortuneCreateRequest.builder()
            .description(description)
            .build();
        
        given(fortuneService.generateFortune(request.toServiceRequest(memberId)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("오늘의 포춘쿠키 생성 시, 설명은 필수이다.")
    @Test
    void generateFortune_WithNonDescription() throws Exception {
        // given
        var request = FortuneCreateRequest.builder()
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    
    @TestMember
    @DisplayName("오늘의 포춘쿠키를 배정받는다.")
    @Test
    void assignFortune() throws Exception {
        // given
        long memberId = 1L;
        String description = "행운이 가득한 하루!";
        FortuneResponse response = FortuneResponse.builder()
            .description(description)
            .build();
        given(fortuneService.assignFortune(memberId)).willReturn(response);
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/assign")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value(description));
    }
    
}