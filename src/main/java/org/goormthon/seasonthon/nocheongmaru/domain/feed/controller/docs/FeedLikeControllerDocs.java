package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.docs;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
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

public interface FeedLikeControllerDocs {

	@Operation(
		summary = "피드 좋아요 토글",
		description = """
          특정 피드의 좋아요 상태를 **토글**합니다.
          - 이미 좋아요 상태면 **취소**, 아니라면 **등록**됩니다.
          - 로그인/인증이 필요합니다.
          """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토글 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = FeedLikeResponse.class),
					examples = @ExampleObject(
						name = "좋아요 토글 응답 예시(좋아요 등록됨)",
						value = """
                          {
                            "feedId": 101,
                            "liked": true,
                            "likeCount": 12
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "401", description = "인증 필요(토큰 없음/만료)"),
		@ApiResponse(responseCode = "404", description = "피드 없음")
	})
	ResponseEntity<FeedLikeResponse> toggle(
		@Parameter(description = "대상 피드 ID", example = "101", required = true, in = ParameterIn.PATH)
		Long feedId,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);
}
