package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "토큰 응답")
public record TokenResponse(
    
    @Schema(description = "새로 발급된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ...")
    String accessToken,
    
    @Schema(description = "새로 발급된 리프레시 토큰", example = "new_refresh_token_example_67890")
    String refreshToken
    
) {

}
