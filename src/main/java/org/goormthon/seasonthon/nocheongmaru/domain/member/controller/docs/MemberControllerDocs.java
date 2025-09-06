package org.goormthon.seasonthon.nocheongmaru.domain.member.controller.docs;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
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
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Member", description = "회원 관련 API (FCM 토큰 등록 등)")
public interface MemberControllerDocs {
    
    @Operation(
        summary = "회원 정보 반환",
        description = """
            특정 회원의 정보르 반환합니다.
            """
    )
    ResponseEntity<MemberDetailResponse> getMemberDetail(
        @Parameter(hidden = true) @AuthMemberId Long memberId
    );
    
    @Operation(
        summary = "회원 FCM 토큰 등록",
        description = """
            특정 회원의 **FCM 토큰**을 등록/갱신합니다.  
            - 클라이언트 앱에서 받은 FCM 토큰을 전달해야 합니다.  
            - 토큰이 비어있으면 400 에러를 반환합니다.
            """
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(
                name = "요청 예시",
                value = """
                    {
                      "token": "fcm-token-from-client-abc123..."
                    }
                    """
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "등록/갱신 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "성공 응답 예시",
                    value = """
                        {
                          "message": "FCM 토큰이 정상적으로 등록되었습니다."
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (토큰 누락/비어있음)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "에러 응답 예시",
                    value = """
                        {
                          "message": "토큰이 비어 있습니다."
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "해당 회원 없음")
    })
    ResponseEntity<Map<String, String>> registerFcmToken(
        @Parameter(description = "FCM 토큰을 등록할 회원 ID", example = "123", required = true, in = ParameterIn.PATH)
        Long memberId,
        Map<String, String> request
    );
}
