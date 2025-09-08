package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request.MissionCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request.MissionModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissionControllerTest extends ControllerTestSupport {
    
    private static final String ADMIN_BASE_URL = "/api/v1/missions/admin";
    private static final String BASE_URL = "/api/v1/missions";
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 생성한다.")
    @Test
    void generateMission() throws Exception {
        // given
        Long memberId = 1L;
        String description = "description";
        
        var request = MissionCreateRequest.builder()
            .description(description)
            .build();
        
        given(missionService.generate(request.toServiceRequest(memberId)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                post(ADMIN_BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1L));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 생성할 때, 미션 설명이 없으면 예외가 발생한다.")
    @Test
    void generateMission_WithNonTitle() throws Exception {
        // given
        var request = MissionCreateRequest.builder()
            .build();
        
        // expected
        mockMvc.perform(
                post(ADMIN_BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("미션 설명은 필수입니다."));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 자신의 미션 목록을 조회한다.")
    @Test
    void getMissionsByMember() throws Exception {
        // given
        Long memberId = 1L;
        var response1 = createMissionResponse(1L, "desc1");
        var response2 = createMissionResponse(2L, "desc2");
        given(missionService.getMissionsByMember(memberId))
            .willReturn(java.util.List.of(response1, response2));
        
        // expected
        mockMvc.perform(get(ADMIN_BASE_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].description").value("desc1"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].description").value("desc2"));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 자신의 미션을 상세 조회한다.")
    @Test
    void getMissionByMember() throws Exception {
        // given
        Long missionId = 5L;
        var response = createMissionResponse(missionId, "detailed description");
        given(missionService.getMissionByMember(missionId))
            .willReturn(response);
        
        // expected
        mockMvc.perform(get(ADMIN_BASE_URL + "/" + missionId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(missionId))
            .andExpect(jsonPath("$.description").value("detailed description"));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 수정한다.")
    @Test
    void modifyMission() throws Exception {
        // given
        Long memberId = 1L;
        Long missionId = 10L;
        String newDescription = "updated description";
        var request = MissionModifyRequest.builder()
            .missionId(missionId)
            .description(newDescription)
            .build();
        
        willDoNothing().given(missionService).modify(request.toServiceRequest(memberId));
        // expected
        mockMvc.perform(
                put(ADMIN_BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 수정할 때, 미션 설명이 없으면 예외가 발생한다.")
    @Test
    void modifyMission_WithNonDescription() throws Exception {
        // given
        Long missionId = 10L;
        var request = MissionModifyRequest.builder()
            .missionId(missionId)
            .build();
        
        // expected
        mockMvc.perform(
                put(ADMIN_BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("미션 설명은 필수입니다."));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 수정할 때, 미션 ID가 없으면 예외가 발생한다.")
    @Test
    void modifyMission_WithNonMissionId() throws Exception {
        // given
        String newDescription = "updated description";
        var request = MissionModifyRequest.builder()
            .description(newDescription)
            .build();
        
        // expected
        mockMvc.perform(
                put(ADMIN_BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.missionId").value("미션 ID는 필수입니다."));
    }
    
    @TestMember(roles = {"ADMIN"})
    @DisplayName("관리자가 미션을 삭제한다.")
    @Test
    void deleteMission() throws Exception {
        // given
        Long memberId = 1L;
        Long missionId = 5L;
        willDoNothing().given(missionService).delete(memberId, missionId);
        
        // expected
        mockMvc.perform(delete(ADMIN_BASE_URL + "/" + missionId))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
    
    @TestMember
    @DisplayName("회원에게 배정된 미션을 조회한다.")
    @Test
    void getAllocatedMission() throws Exception {
        // given
        Long memberId = 1L;
        var response = createMissionResponse(7L, "today's mission");
        given(missionService.getAllocatedMission(memberId))
            .willReturn(response);
        
        // expected
        mockMvc.perform(get(BASE_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(7L))
            .andExpect(jsonPath("$.description").value("today's mission"));
    }
    
    private MissionResponse createMissionResponse(Long missionId, String description) {
        return MissionResponse.builder()
            .id(missionId)
            .description(description)
            .build();
    }
    
}