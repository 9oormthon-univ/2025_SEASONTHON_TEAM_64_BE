package org.goormthon.seasonthon.nocheongmaru.domain.information.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.ApiExceptions;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.springframework.http.ResponseEntity;

@Tag(name = "Information", description = "정보 관리 API - 병원, 카페, 레스토랑 등의 정보를 등록하고 관리하는 기능을 제공합니다.")
public interface InformationControllerDocs {
    
    @ApiExceptions({ErrorCode.UNAUTHORIZED, ErrorCode.ADDRESS_NOT_FOUND})
    @Operation(
        summary = "정보 등록",
        description = """
            새로운 정보(병원, 카페, 레스토랑 등)를 등록합니다.
            - 사용자 인증이 필요합니다. (JWT 토큰)
            - 제목, 설명, 주소, 카테고리는 모두 필수 입력값입니다.
            - 설명은 최대 200자까지 입력 가능합니다.
            - 카테고리는 HOSPITAL_FACILITIES, RESTAURANT_CAFE, ETC 중 하나를 선택해야 합니다.
            - 등록 성공 시 생성된 정보의 ID를 반환합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "정보 등록 성공 - 생성된 정보의 ID를 반환합니다.",
            content = @Content(
                schema = @Schema(type = "integer", format = "int64", example = "1"),
                examples = @ExampleObject(
                    name = "정보 등록 성공 응답",
                    value = "1"
                )
            )
        ),
    })
    ResponseEntity<Long> createInformation(
        @Parameter(
            hidden = true
        ) Long memberId,
        
        @RequestBody(
            description = "정보 등록 요청 데이터",
            required = true,
            content = @Content(
                schema = @Schema(implementation = InformationCreateRequest.class),
                examples = {
                    @ExampleObject(
                        name = "병원 정보 등록 예시",
                        value = """
                            {
                                "title": "서울대학교병원",
                                "description": "대한민국 최고 수준의 의료 서비스를 제공하는 종합병원입니다. 각종 전문 진료과와 최신 의료 장비를 갖추고 있습니다.",
                                "address": "서울특별시 종로구 대학로 101",
                                "category": "HOSPITAL_FACILITIES"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "카페 정보 등록 예시",
                        value = """
                            {
                                "title": "스타벅스 강남점",
                                "description": "조용하고 깔끔한 분위기의 카페입니다. 와이파이가 잘 되어 작업하기 좋고, 다양한 음료와 디저트를 제공합니다.",
                                "address": "서울특별시 강남구 테헤란로 123",
                                "category": "RESTAURANT_CAFE"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "기타 정보 등록 예시",
                        value = """
                            {
                                "title": "시청 민원실",
                                "description": "각종 민원 업무를 처리할 수 있는 곳입니다. 주민등록등본, 인감증명서 등을 발급받을 수 있습니다.",
                                "address": "서울특별시 중구 세종대로 110",
                                "category": "ETC"
                            }
                            """
                    )
                }
            )
        ) InformationCreateRequest request
    );
}
