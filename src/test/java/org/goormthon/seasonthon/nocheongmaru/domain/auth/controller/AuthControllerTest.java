package org.goormthon.seasonthon.nocheongmaru.domain.auth.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.dto.request.ReissueTokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/auth";
    
    @DisplayName("토큰 재발급을 한다.")
    @Test
    void reissueToken() throws Exception {
        // given
        ReissueTokenRequest request = ReissueTokenRequest.builder()
            .accessToken("valid-access-token")
            .refreshToken("valid-refresh-token")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/reissue")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @DisplayName("토큰 재발급 시, 액세스 토큰은 필수이다.")
    @Test
    void reissueToken_WithNonAccessToken() throws Exception {
        // given
        ReissueTokenRequest request = ReissueTokenRequest.builder()
            .refreshToken("valid-refresh-token")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/reissue")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.accessToken").value("액세스 토큰은 필수입니다."));
    }
    
    @DisplayName("토큰 재발급 시, 리프레시 토큰은 필수이다.")
    @Test
    void reissueToken_WithNonRefreshToken() throws Exception {
        // given
        ReissueTokenRequest request = ReissueTokenRequest.builder()
            .accessToken("valid-access-token")
            .build();
        
        // expected
        mockMvc.perform(
                post(BASE_URL + "/reissue")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.refreshToken").value("리프레시 토큰은 필수입니다."));
    }
    
}