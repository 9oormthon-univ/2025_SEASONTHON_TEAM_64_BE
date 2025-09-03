package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.docs;

import java.time.LocalDate;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.AssignmentListResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.response.MemberTodayMissionResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Mission", description = "미션 배정 조회 API")
public interface MissionControllerDocs {

	@Operation(
		summary = "특정 날짜의 미션 배정 현황 조회",
		description = """
          선택한 날짜(`yyyy-MM-dd`, KST 기준)의 회원별 미션 배정 현황을 조회합니다.
          - `date`를 생략하면 **오늘(LocalDate.now(KST))** 기준으로 조회합니다.
          """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = AssignmentListResponse.class),
					examples = @ExampleObject(
						name = "배정 현황 응답 예시",
						value = """
                          {
                            "date": "2025-09-04",
                            "items": [
                              {
                                "memberId": 1,
                                "memberNickname": "초코",
                                "missionId": 10,
                                "missionTitle": "물 2L 마시기",
                                "status": "ASSIGNED"
                              },
                              {
                                "memberId": 2,
                                "memberNickname": "바닐라",
                                "missionId": 11,
                                "missionTitle": "만보 걷기",
                                "status": "COMPLETED"
                              }
                            ]
                          }
                          """
					)
				)
			}
		)
	})
	ResponseEntity<AssignmentListResponse> getAssignmentsByDate(
		@Parameter(
			description = "조회할 날짜(ISO-8601, `yyyy-MM-dd`). 미입력 시 오늘(KST).",
			example = "2025-09-04",
			in = ParameterIn.QUERY
		)
		LocalDate date
	);

	@Operation(
		summary = "회원의 오늘 미션 조회",
		description = """
          특정 회원의 **오늘(KST)** 미션과 상태를 조회합니다.
          """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = MemberTodayMissionResponse.class),
					examples = @ExampleObject(
						name = "오늘 미션 응답 예시",
						value = """
                          {
                            "memberId": 1,
                            "date": "2025-09-04",
                            "mission": { "missionId": 10, "title": "물 2L 마시기" },
                            "status": "ASSIGNED"
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "404", description = "해당 회원/미션 데이터 없음")
	})
	ResponseEntity<MemberTodayMissionResponse> getMemberTodayMission(
		@Parameter(description = "대상 회원 ID", example = "1", required = true, in = ParameterIn.PATH)
		Long memberId
	);
}
