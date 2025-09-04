package org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "정보나눔 피드 목록 응답 데이터")
public record InformationResponse(
    
    @Schema(
        description = "정보나눔 피드 ID",
        example = "1"
    )
    Long informationId,
    
    @Schema(
        description = "정보나눔 피드 제목",
        example = "서울대학교병원"
    )
    String title,
    
    @Schema(
        description = "카테고리",
        example = "HOSPITAL_FACILITIES",
        allowableValues = {"HOSPITAL_FACILITIES", "RESTAURANT_CAFE", "ETC"}
    )
    String category,
    
    @Schema(
        description = "주소",
        example = "서울특별시 종로구 대학로 101"
    )
    String address,
    
    @Schema(
        description = "대표 이미지 URL",
        example = "https://example.com/images/hospital.jpg"
    )
    String imageUrl
    
) {
}
