package org.goormthon.seasonthon.nocheongmaru.domain.information.controller.docs;

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
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.ApiExceptions;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Information", description = "정보나눔 피드 관리 API - 병원, 카페, 레스토랑 등의 정보를 등록하고 관리하는 기능을 제공합니다.")
public interface InformationControllerDocs {
    
    @Operation(
        summary = "정보나눔 피드 목록 조회",
        description = """
            정보나눔 피드 목록을 조회합니다.
            
            **파라미터 설명:**
            - lastId: 커서 기반 페이지네이션을 위한 마지막 조회 피드 ID (다음 페이지 조회 시 사용)
            - category: 카테고리별 필터링 (HOSPITAL_FACILITIES, RESTAURANT_CAFE, ETC)
            - sortByRecent: 정렬 방식 (true: 최신순, false: 오래된순, 기본값: true)
            
            **페이지네이션:**
            - 한 번에 최대 8개의 피드를 반환합니다.
            - 다음 페이지를 조회하려면 응답의 마지막 피드 ID를 lastId로 전달하세요.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "정보나눔 피드 목록 조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InformationResponse.class),
                examples = @ExampleObject(
                    name = "정보나눔 피드 목록 응답 예시",
                    value = """
                        [
                          {
                            "informationId": 1,
                            "title": "서울대학교병원",
                            "category": "HOSPITAL_FACILITIES",
                            "address": "서울특별시 종로구 대학로 101",
                            "imageUrl": "https://example.com/images/hospital.jpg"
                          },
                          {
                            "informationId": 2,
                            "title": "스타벅스 강남점",
                            "category": "RESTAURANT_CAFE",
                            "address": "서울특별시 강남구 테헤란로 123",
                            "imageUrl": "https://example.com/images/cafe.jpg"
                          }
                        ]
                        """
                )
            )
        )
    })
    ResponseEntity<List<InformationResponse>> getInformationList(
        @Parameter(
            description = "커서 기반 페이지네이션을 위한 마지막 조회 피드 ID",
            example = "10",
            in = ParameterIn.QUERY
        )
        @RequestParam(required = false) Long lastId,
        
        @Parameter(
            description = "카테고리별 필터링",
            example = "HOSPITAL_FACILITIES",
            schema = @Schema(allowableValues = {"HOSPITAL_FACILITIES", "RESTAURANT_CAFE", "ETC"}),
            in = ParameterIn.QUERY
        )
        @RequestParam(required = false) String category,
        
        @Parameter(
            description = "정렬 방식 (true: 최신순, false: 오래된순, 기본값: true)",
            example = "true",
            in = ParameterIn.QUERY
        )
        @RequestParam(required = false) Boolean sortByRecent
    );
    
    @Operation(
        summary = "정보나눔 피드 등록",
        description = """
            새로운 정보나눔 피드를 등록합니다.
            
            **요청 형식:**
            - Content-Type: multipart/form-data
            - request: JSON 형태의 피드 정보
            - images: 이미지 파일들 (선택사항, 여러 개 업로드 가능)
            
            **주의사항:**
            - 제목, 설명, 주소, 카테고리는 필수 입력값입니다.
            - 설명은 최대 200자까지 입력 가능합니다.
            - 카테고리는 HOSPITAL_FACILITIES, RESTAURANT_CAFE, ETC 중 하나여야 합니다.
            """
    )
    @RequestBody(
        content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            examples = @ExampleObject(
                name = "정보나눔 피드 등록 요청 예시",
                value = """
                    {
                      "title": "서울대학교병원",
                      "description": "대한민국 최고 수준의 의료 서비스를 제공하는 종합병원입니다.",
                      "address": "서울특별시 종로구 대학로 101",
                      "category": "HOSPITAL_FACILITIES"
                    }
                    """
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "정보나눔 피드 등록 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(type = "integer", format = "int64"),
                examples = @ExampleObject(
                    name = "등록된 피드 ID",
                    value = "1"
                )
            )
        ),
    })
    @ApiExceptions({
        ErrorCode.ADDRESS_NOT_FOUND,
        ErrorCode.IMAGE_UPLOAD_FAILED,
        ErrorCode.KAKAO_HTTP_CLIENT_ERROR
    })
    ResponseEntity<Long> createInformation(
        @Parameter(hidden = true) @AuthMemberId Long memberId,
        @RequestPart @Valid InformationCreateRequest request,
        @RequestPart(required = false) List<MultipartFile> images
    );
    
    @Operation(
        summary = "정보나눔 피드 상세 조회",
        description = "특정 정보나눔 피드의 상세 정보를 조회합니다. 작성자 정보, 모든 이미지, 위치 정보 등이 포함됩니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "정보나눔 피드 상세 조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = InformationDetailResponse.class),
                examples = @ExampleObject(
                    name = "정보나눔 피드 상세 응답 예시",
                    value = """
                        {
                          "informationId": 1,
                          "title": "서울대학교병원",
                          "description": "대한민국 최고 수준의 의료 서비스를 제공하는 종합병원입니다.",
                          "category": "HOSPITAL_FACILITIES",
                          "address": "서울특별시 종로구 대학로 101",
                          "latitude": 37.5796,
                          "longitude": 126.9669,
                          "createdAt": "2024-03-15",
                          "writer": {
                            "memberId": 1,
                            "nickname": "의료정보제공자",
                            "profileImageUrl": "https://example.com/profile.jpg",
                            "role": "ROLE_USER"
                          },
                          "images": [
                            {
                              "imageId": 1,
                              "imageUrl": "https://example.com/images/hospital1.jpg"
                            },
                            {
                              "imageId": 2,
                              "imageUrl": "https://example.com/images/hospital2.jpg"
                            }
                          ]
                        }
                        """
                )
            )
        ),
    })
    @ApiExceptions({
        ErrorCode.INFORMATION_NOT_FOUND,
    })
    ResponseEntity<InformationDetailResponse> getInformationDetail(
        @Parameter(
            description = "조회할 정보나눔 피드 ID",
            example = "1",
            required = true
        )
        @PathVariable Long informationId
    );
    
    @Operation(
        summary = "정보나눔 피드 수정",
        description = """
            기존 정보나눔 피드를 수정합니다.
            
            **권한:**
            - 피드 작성자만 수정할 수 있습니다.
            
            **요청 형식:**
            - Content-Type: multipart/form-data
            - request: JSON 형태의 수정할 피드 정보
            - images: 새로운 이미지 파일들 (선택사항, 기존 이미지는 모두 삭제되고 새로 등록됩니다)
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "정보나눔 피드 수정 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(type = "integer", format = "int64"),
                examples = @ExampleObject(
                    name = "수정된 피드 ID",
                    value = "1"
                )
            )
        ),
    })
    @ApiExceptions({
        ErrorCode.INFORMATION_NOT_FOUND,
        ErrorCode.IS_NOT_INFORMATION_OWNER,
    })
    ResponseEntity<Long> modifyInformation(
        @Parameter(hidden = true) @AuthMemberId Long memberId,
        @Parameter(
            description = "수정할 정보나눔 피드 ID",
            example = "1",
            required = true
        )
        @PathVariable Long informationId,
        @RequestPart @Valid InformationModifyRequest request,
        @RequestPart(required = false) List<MultipartFile> images
    );
    
    @Operation(
        summary = "정보나눔 피드 삭제",
        description = """
            정보나눔 피드를 삭제합니다.
            
            **권한:**
            - 피드 작성자만 삭제할 수 있습니다.
            
            **주의사항:**
            - 삭제된 피드는 복구할 수 없습니다.
            - 연관된 이미지들도 함께 삭제됩니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "정보나눔 피드 삭제 성공"
        ),
    })
    @ApiExceptions({
        ErrorCode.INFORMATION_NOT_FOUND,
        ErrorCode.IS_NOT_INFORMATION_OWNER
    })
    ResponseEntity<Void> deleteInformation(
        @Parameter(hidden = true) @AuthMemberId Long memberId,
        @Parameter(
            description = "삭제할 정보나눔 피드 ID",
            example = "1",
            required = true
        )
        @PathVariable Long informationId
    );
    
}
