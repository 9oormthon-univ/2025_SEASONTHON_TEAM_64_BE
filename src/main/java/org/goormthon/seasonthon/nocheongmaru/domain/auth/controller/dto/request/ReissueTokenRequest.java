package org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.dto.request.ReissueTokenServiceRequest;


import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(description = "토큰 재발급 요청")
public record ReissueTokenRequest(
    
    @Schema(description = "만료된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ...", requiredMode = REQUIRED)
    @NotBlank(message = "액세스 토큰은 필수입니다.")
    String accessToken,
    
    @Schema(description = "유효한 리프레시 토큰", example = "refresh_token_example_12345", requiredMode = REQUIRED)
    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    String refreshToken
    
) {

    public ReissueTokenServiceRequest toServiceRequest() {
        return ReissueTokenServiceRequest.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
    
}
