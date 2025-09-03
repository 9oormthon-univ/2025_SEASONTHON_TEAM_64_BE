package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.docs;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
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

@Tag(name = "Feed", description = "피드 CRUD 및 커서 페이지네이션 API")
public interface FeedControllerDocs {

	@Operation(
		summary = "피드 커서 페이지 조회",
		description = """
          커서 기반으로 피드 목록을 조회합니다.
          - `cursorId`를 전달하면 해당 ID 이후의 데이터부터 조회합니다.
          - `size`는 페이지 크기(기본 5)입니다.
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
						name = "커서 페이지 응답 예시",
						value = """
                          {
                            "items": [
                              {
                                "feedId": 101,
                                "description": "하루 미션 인증!",
                                "imageUrl": "https://cdn.example.com/images/101.jpg",
                                "member": {
                                  "memberId": 12,
                                  "nickname": "하루한걸음",
                                  "profileImageUrl": "https://cdn.example.com/profile/12.png",
                                  "role": "ROLE_USER"
                                },
                                "likeCount": 3,
                                "commentCount": 2,
                                "missionId": 7
                              }
                            ],
                            "nextCursorId": 100,
                            "hasNext": true
                          }
                          """
					)
				)
			}
		)
	})
	ResponseEntity<CursorPageResponse<FeedResponse>> getFeeds(
		@Parameter(description = "다음 페이지 조회를 위한 마지막 피드 ID", example = "100", in = ParameterIn.QUERY)
		Long cursorId,
		@Parameter(description = "페이지 크기(기본 5)", example = "5", in = ParameterIn.QUERY)
		int size
	);

	@Operation(summary = "피드 단건 조회", description = "feedId로 특정 피드를 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = {
				@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = FeedResponse.class),
					examples = @ExampleObject(
						name = "피드 단건 응답 예시",
						value = """
                          {
                            "feedId": 101,
                            "description": "런닝 5km 성공!",
                            "imageUrl": "https://cdn.example.com/images/101.jpg",
                            "member": {
                              "memberId": 12,
                              "nickname": "러너",
                              "profileImageUrl": "https://cdn.example.com/profile/12.png",
                              "role": "ROLE_USER"
                            },
                            "likeCount": 10,
                            "commentCount": 4,
                            "missionId": 7
                          }
                          """
					)
				)
			}
		),
		@ApiResponse(responseCode = "404", description = "피드 없음")
	})
	ResponseEntity<FeedResponse> getFeedsById(
		@Parameter(description = "조회할 피드 ID", example = "101", required = true, in = ParameterIn.PATH)
		Long feedId
	);

	@Operation(
		summary = "피드 생성",
		description = """
          새 피드를 생성합니다. (작성자 인증 필요)
          - 생성 성공 시 Location 헤더와 함께 **201 Created**를 반환합니다.
          """
	)
	@RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = FeedRequest.class),
				examples = @ExampleObject(
					name = "피드 생성 요청 예시",
					value = """
                      {
                        "description": "오늘의 미션 완료!",
                        "imageUrl": "https://cdn.example.com/images/999.jpg",
                        "missionId": 7
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
					schema = @Schema(implementation = FeedResponse.class)
				)
			}
		),
		@ApiResponse(responseCode = "400", description = "검증 실패"),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "404", description = "회원 없음 / 미션 없음")
	})
	ResponseEntity<FeedResponse> createFeed(
		@Valid FeedRequest request,
		@Parameter(hidden = true) @AuthMemberId Long memberId,
		@Parameter(hidden = true) UriComponentsBuilder uriBuilder
	);

	@Operation(
		summary = "피드 삭제",
		description = "본인이 작성한 피드를 삭제합니다. 성공 시 200 OK와 메시지를 반환합니다."
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
		@ApiResponse(responseCode = "404", description = "피드 없음")
	})
	ResponseEntity<Map<String, String>> deleteFeed(
		@Parameter(description = "삭제할 피드 ID", example = "345", required = true, in = ParameterIn.PATH)
		Long feedId,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);

	@Operation(
		summary = "피드 전체 수정(교체)",
		description = "지정한 피드를 **전체 교체(put)** 합니다. (모든 필드 필수)"
	)
	@RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = FeedPutRequest.class),
				examples = @ExampleObject(
					name = "피드 수정 요청 예시",
					value = """
                      {
                        "description": "내용 전체 교체",
                        "imageUrl": "https://cdn.example.com/images/updated.jpg",
                        "missionId": 9
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
					schema = @Schema(implementation = FeedResponse.class)
				)
			}
		),
		@ApiResponse(responseCode = "400", description = "검증 실패"),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "403", description = "본인 아님"),
		@ApiResponse(responseCode = "404", description = "피드 없음 / 미션 없음")
	})
	ResponseEntity<FeedResponse> putFeed(
		@Parameter(description = "수정할 피드 ID", example = "345", required = true, in = ParameterIn.PATH)
		Long feedId,
		@Valid FeedPutRequest request,
		@Parameter(hidden = true) @AuthMemberId Long memberId
	);
}
