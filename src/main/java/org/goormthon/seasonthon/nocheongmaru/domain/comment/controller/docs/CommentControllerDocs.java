package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.docs;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

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
import jakarta.validation.Valid;

@Tag(name = "Comment", description = "댓글 커서 조회/생성/수정/삭제 API")
public interface CommentControllerDocs {

	@Operation(
		summary = "댓글 커서 페이지 조회",
		description = """
          특정 피드의 댓글을 커서 기반으로 조회합니다.
          - `cursorId` 전달 시 해당 ID 이후의 댓글부터 조회
          - `size`는 페이지 크기(기본 10)
          """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CursorPageResponse.class),
					examples = @ExampleObject(
						name = "커서 댓글 목록 응답 예시",
						value = """
                          {
                            "items": [
                              {
                                "commentId": 501,
                                "feedId": 100,
                                "memberId": 12,
                                "description": "멋져요!",
                                "createdAt": "2025-09-01T12:34:56"
                              }
                            ],
                            "nextCursorId": 500,
                            "hasNext": true
                          }
                          """
					)
				)
			}
		)
	})
	ResponseEntity<CursorPageResponse<CommentResponse>> getComments(
		@Parameter(description = "대상 피드 ID", example = "100", required = true, in = ParameterIn.PATH)
		Long feedId,
		@Parameter(description = "다음 페이지 조회를 위한 마지막 댓글 ID", example = "500", in = ParameterIn.QUERY)
		Long cursorId,
		@Parameter(description = "페이지 크기(기본 10)", example = "10", in = ParameterIn.QUERY)
		int size,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);

	@Operation(
		summary = "댓글 생성",
		description = """
          특정 피드에 댓글을 생성합니다. (인증 필요)
          - 성공 시 Location 헤더와 함께 201 Created
          """
	)
	@RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = CommentRequest.class),
				examples = @ExampleObject(
					name = "댓글 생성 요청 예시",
					value = """
                      {
                        "description": "정말 유용한 정보네요!"
                      }
                      """
				)
			)
		}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "생성 성공 (Location 헤더 포함)",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CommentResponse.class),
					examples = @ExampleObject(
						name = "댓글 생성 응답 예시",
						value = """
                          {
                            "commentId": 777,
                            "feedId": 100,
                            "memberId": 12,
                            "description": "정말 유용한 정보네요!",
                            "createdAt": "2025-09-03T09:15:00"
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "400", description = "검증 실패"),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "404", description = "피드 없음")
	})
	ResponseEntity<CommentResponse> createComment(
		@Parameter(description = "대상 피드 ID", example = "100", required = true, in = ParameterIn.PATH)
		Long feedId,
		@Valid CommentRequest request,
		@Parameter(hidden = true) @AuthMemberId Long memberId,
		@Parameter(hidden = true) UriComponentsBuilder uriBuilder
	);

	@Operation(
		summary = "댓글 삭제",
		description = "본인이 작성한 댓글을 삭제합니다. 성공 시 200 OK와 메시지 반환."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "삭제 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Map.class),
					examples = @ExampleObject(
						name = "삭제 응답 예시",
						value = """
							{ "message": "정상적으로 삭제되었습니다." }
							"""
					)
				)
			}
		),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "403", description = "본인 아님"),
		@ApiResponse(responseCode = "404", description = "댓글 없음")
	})
	ResponseEntity<Map<String, String>> deleteComment(
		@Parameter(description = "삭제할 댓글 ID", example = "777", required = true, in = ParameterIn.PATH)
		Long commentId,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);

	@Operation(
		summary = "댓글 전체 수정(교체)",
		description = "댓글을 **전체 교체(put)** 합니다. (모든 필드 필수)"
	)
	@RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = CommentPutRequest.class),
				examples = @ExampleObject(
					name = "댓글 수정 요청 예시",
					value = """
                      {
                        "description": "전체 교체된 댓글 내용"
                      }
                      """
				)
			)
		}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "수정 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CommentResponse.class),
					examples = @ExampleObject(
						name = "댓글 수정 응답 예시",
						value = """
                          {
                            "commentId": 777,
                            "feedId": 100,
                            "memberId": 12,
                            "description": "전체 교체된 댓글 내용",
                            "createdAt": "2025-09-03T09:20:00"
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "400", description = "검증 실패"),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "403", description = "본인 아님"),
		@ApiResponse(responseCode = "404", description = "댓글 없음")
	})
	ResponseEntity<CommentResponse> putComment(
		@Parameter(description = "수정할 댓글 ID", example = "777", required = true, in = ParameterIn.PATH)
		Long commentId,
		@Valid CommentPutRequest request,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);
}
