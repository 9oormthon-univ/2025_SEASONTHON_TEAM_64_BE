package org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.dto.request.ReissueTokenRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.ApiExceptions;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증/토큰 관련 API - JWT 토큰 관리 기능을 제공합니다.")
public interface AuthControllerDocs {
    
    @ApiExceptions({ErrorCode.INVALID_ACCESS_TOKEN, ErrorCode.INVALID_REFRESH_TOKEN, ErrorCode.UNAUTHORIZED})
    @Operation(
        summary = "JWT 토큰 재발급",
        description = """
            만료된 액세스 토큰과 유효한 리프레시 토큰을 사용하여 새로운 토큰 쌍을 발급받습니다.
            - 액세스 토큰과 리프레시 토큰 모두 필수 입력값입니다.
            - 리프레시 토큰이 유효한 경우 새로운 액세스 토큰과 리프레시 토큰을 반환합니다.
            - 토큰이 유효하지 않은 경우 401 Unauthorized가 반환됩니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공 - 새로운 액세스 토큰과 리프레시 토큰을 반환합니다.",
            content = @Content(
                schema = @Schema(implementation = TokenResponse.class),
                examples = @ExampleObject(
                    name = "토큰 재발급 성공 응답",
                    value = """
                        {
                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ...",
                            "refreshToken": "new_refresh_token_example_67890"
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<TokenResponse> reissueToken(
        @RequestBody(
            description = "토큰 재발급 요청 정보",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ReissueTokenRequest.class),
                examples = @ExampleObject(
                    name = "토큰 재발급 요청 예시",
                    value = """
                        {
                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ...",
                            "refreshToken": "refresh_token_example_12345"
                        }
                        """
                )
            )
        ) ReissueTokenRequest reissueTokenRequest
    );
}
