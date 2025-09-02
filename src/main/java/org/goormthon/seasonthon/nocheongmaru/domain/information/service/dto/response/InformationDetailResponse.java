package org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto.ImageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;

import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "정보나눔 피드 상세 응답 데이터")
public record InformationDetailResponse(
    
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
        description = "정보나눔 피드 상세 설명",
        example = "대한민국 최고 수준의 의료 서비스를 제공하는 종합병원입니다. 각종 전문 진료과와 최신 의료 장비를 갖추고 있습니다."
    )
    String description,
    
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
        description = "위도",
        example = "37.5796"
    )
    Double latitude,
    
    @Schema(
        description = "경도",
        example = "126.9669"
    )
    Double longitude,
    
    @Schema(
        description = "작성일",
        example = "2024-03-15"
    )
    LocalDate createdAt,
    
    @Schema(
        description = "관련 이미지 목록"
    )
    List<ImageResponse> images,
    
    @Schema(
        description = "작성자 정보"
    )
    MemberDetailResponse writer
    
) {
}
