package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.docs;

import java.util.Map;

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

@Tag(name = "Member Push", description = "미션 배정 트리거 및 FCM 토큰 등록 API")
public interface MemberPushControllerDocs {

	@Operation(
		summary = "오늘자 미션 배정 및 즉시 FCM 발송 트리거",
		description = """
          서버에서 **오늘 날짜 기준**으로 모든 회원에게 미션을 배정하고,
          각 회원의 FCM 토큰이 있는 경우 **즉시 푸시 알림**을 발송합니다.
          (보통 내부/관리자용 엔드포인트)
          """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "트리거 완료",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Map.class),
					examples = @ExampleObject(
						name = "성공 응답 예시",
						value = """
                          {
                            "message": "오늘 날짜 기준 미션 배정 및 FCM 발송 트리거 완료",
                            "triggeredAt": "2025-09-04T14:30:00+09:00[Asia/Seoul]"
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "403", description = "권한 없음(예: 관리자 전용)")
	})
	ResponseEntity<Map<String, Object>> assignTodayAndNotifyNow();

	@Operation(
		summary = "회원 FCM 토큰 등록/갱신",
		description = """
          클라이언트(앱)에서 받은 **FCM 토큰**을 서버에 등록/갱신합니다.
          - 등록 후에는 서버 알림 발송 시 해당 토큰이 사용됩니다.
          - 동일 회원이 토큰을 바꾸면 최신 토큰으로 갱신됩니다.
          """
	)
	@RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = Map.class),
				examples = @ExampleObject(
					name = "요청 바디 예시",
					value = """
                      {
                        "token": "fcm-token-from-client-abc123..."
                      }
                      """
				)
			)
		}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "등록/갱신 성공(본문 없음)"),
		@ApiResponse(responseCode = "400", description = "요청 형식 오류(예: token 누락)"),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "404", description = "회원 없음")
	})
	ResponseEntity<Void> registerFcmToken(
		@Parameter(description = "FCM 토큰을 등록할 회원 ID", example = "123", required = true, in = ParameterIn.PATH)
		Long memberId,
		Map<String, String> req
	);
}
