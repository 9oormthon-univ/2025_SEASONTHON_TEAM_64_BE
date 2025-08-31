package org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request.InformationCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.ValidEnum;
import org.hibernate.validator.constraints.Length;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(description = "정보 등록 요청 데이터")
public record InformationCreateRequest(

    @Schema(
        description = "정보 제목",
        example = "서울대학교병원",
        requiredMode = REQUIRED
    )
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    String title,
    
    @Schema(
        description = "정보 상세 설명 (최대 200자)",
        example = "대한민국 최고 수준의 의료 서비스를 제공하는 종합병원입니다. 각종 전문 진료과와 최신 의료 장비를 갖추고 있습니다.",
        maxLength = 200,
        requiredMode = REQUIRED
    )
    @Length(max = 200, message = "설명은 최대 200자까지 입력 가능합니다.")
    @NotBlank(message = "설명은 필수 입력 값입니다.")
    String description,
    
    @Schema(
        description = "정보 주소",
        example = "서울특별시 종로구 대학로 101",
        requiredMode = REQUIRED
    )
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String address,
    
    @Schema(
        description = "정보 카테고리",
        example = "HOSPITAL_FACILITIES",
        allowableValues = {"HOSPITAL_FACILITIES", "RESTAURANT_CAFE", "ETC"},
        requiredMode = REQUIRED
    )
    @ValidEnum(verifyClass = Category.class, message = "정확한 카테고리를 입력해주세요.")
    String category
    
) {
    
    public InformationCreateServiceRequest toServiceRequest(Long memberId) {
        return InformationCreateServiceRequest.builder()
            .memberId(memberId)
            .title(title)
            .description(description)
            .address(address)
            .category(Category.toCategory(category))
            .build();
    }
    
}
