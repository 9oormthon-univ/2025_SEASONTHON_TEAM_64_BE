package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.docs;

import java.util.List;
import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.MissionResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Mission Admin", description = "관리자용 미션 등록/삭제/조회 API")
public interface MissionAdminControllerDocs {

	@Operation(
		summary = "미션 등록",
		description = """
            새로운 미션을 등록합니다.  
            (보통 관리자가 미션 풀을 관리할 때 사용)
            """
	)
	@RequestBody(
		required = true,
		content = @Content(
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = Map.class),
			examples = @ExampleObject(
				name = "요청 바디 예시",
				value = """
                    {
                      "title": "물 2L 마시기"
                    }
                    """
			)
		)
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "등록 성공",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = Mission.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	ResponseEntity<Mission> createMission(Map<String, String> req);


	@Operation(
		summary = "미션 삭제",
		description = "ID 값으로 특정 미션을 삭제합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "삭제 성공 (본문 없음)"),
		@ApiResponse(responseCode = "404", description = "해당 ID의 미션 없음")
	})
	ResponseEntity<Void> deleteMission(
		@Parameter(description = "삭제할 미션의 ID", example = "1", required = true, in = ParameterIn.PATH)
		Long id
	);


	@Operation(
		summary = "미션 목록 조회",
		description = "현재 등록된 모든 미션을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = Mission.class)))
	})
	ResponseEntity<List<MissionResponse>> listMissions();
}
