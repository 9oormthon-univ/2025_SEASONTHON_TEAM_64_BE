package org.goormthon.seasonthon.nocheongmaru.domain.information.controller;

import org.goormthon.seasonthon.nocheongmaru.ControllerTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.image.service.dto.ImageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InformationControllerTest extends ControllerTestSupport {
    
    private static final String BASE_URL = "/api/v1/informations";
    
    @TestMember
    @DisplayName("정보나눔 피드를 생성한다.")
    @Test
    void createInformation() throws Exception {
        // given
        Long memberId = 1L;
        MockMultipartFile imageFile = createMockMultipartFile();
        List<MultipartFile> imagesRequest = List.of(imageFile);
        
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        given(informationService.generateInformation(request.toServiceRequest(memberId, imagesRequest)))
            .willReturn(1L);
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(imageFile)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 제목은 필수이다.")
    @Test
    void createInformation_WithNonTitle() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.title").value("제목은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 설명은 필수이다.")
    @Test
    void createInformation_WithNonDescription() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 설명은 최대 200자까지 입력 가능하다.")
    @Test
    void createInformation_WithTooLongDescription() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("d".repeat(201))
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 최대 200자까지 입력 가능합니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 주소는 필수이다.")
    @Test
    void createInformation_WithNonAddress() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.address").value("주소는 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 카테고리는 필수이다.")
    @Test
    void createInformation_WithNonCategory() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 생성 시, 카테고리는 정확한 값이어야 한다.")
    @Test
    void createInformation_WithInaccurateCategory() throws Exception {
        // given
        var request = InformationCreateRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("INVALID_CATEGORY")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(BASE_URL)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드를 수정한다.")
    @Test
    void modifyInformation() throws Exception {
        // given
        Long memberId = 1L;
        Long informationId = 1L;
        MockMultipartFile imageFile = createMockMultipartFile();
        List<MultipartFile> imagesRequest = List.of(imageFile);
        
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        given(informationService.modifyInformation(request.toServiceRequest(memberId, informationId, imagesRequest)))
            .willReturn(informationId);
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(imageFile)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 제목은 필수이다.")
    @Test
    void modifyInformation_WithNonTitle() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .description("description")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.title").value("제목은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 설명은 필수이다.")
    @Test
    void modifyInformation_WithNonDescription() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 설명은 최대 200자까지 입력 가능하다.")
    @Test
    void modifyInformation_WithTooLongDescription() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("d".repeat(201))
            .address("address")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.description").value("설명은 최대 200자까지 입력 가능합니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 주소는 필수이다.")
    @Test
    void modifyInformation_WithNonAddress() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .category("HOSPITAL_FACILITIES")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.address").value("주소는 필수 입력 값입니다."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 카테고리는 필수이다.")
    @Test
    void modifyInformation_WithNonCategory() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 수정 시, 카테고리는 정확한 값이어야 한다.")
    @Test
    void modifyInformation_WithInaccurateCategory() throws Exception {
        // given
        long informationId = 1L;
        var request = InformationModifyRequest.builder()
            .title("title")
            .description("description")
            .address("address")
            .category("INVALID_CATEGORY")
            .build();
        
        // expected
        mockMvc.perform(
                multipart(HttpMethod.PUT, BASE_URL + "/" + informationId)
                    .file(new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors.category").value("정확한 카테고리를 입력해주세요."));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드를 삭제한다.")
    @Test
    void deleteInformation() throws Exception {
        // given
        long informationId = 1L;
        
        // expected
        mockMvc.perform(
                delete(BASE_URL + "/" + informationId)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 상세 조회를 한다.")
    @Test
    void getInformationDetail() throws Exception {
        // given
        long informationId = 1L;
        
        InformationDetailResponse informationDetailResponse = createInformationDetailResponse();
        given(informationService.getInformationDetail(informationId))
            .willReturn(informationDetailResponse);
        
        // expected
        mockMvc.perform(
                get(BASE_URL + "/" + informationId)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.informationId").value(informationDetailResponse.informationId()))
            .andExpect(jsonPath("$.title").value(informationDetailResponse.title()))
            .andExpect(jsonPath("$.description").value(informationDetailResponse.description()));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 목록을 기본 조회한다.")
    @Test
    void getInformationList_Default() throws Exception {
        // given
        var responses = List.of(
            createInformationListItem(1L, "title1", "HOSPITAL_FACILITIES", "addr1", "img1"),
            createInformationListItem(2L, "title2", "RESTAURANT_CAFE", "addr2", "img2"),
            createInformationListItem(3L, "title3", "ETC", "addr3", "img3")
        );
        given(informationService.getInformationList(null, null, null)).willReturn(responses);
        
        // expected
        mockMvc.perform(
                get(BASE_URL)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(responses.size()))
            .andExpect(jsonPath("$[0].informationId").value(1))
            .andExpect(jsonPath("$[0].title").value("title1"))
            .andExpect(jsonPath("$[0].category").value("HOSPITAL_FACILITIES"))
            .andExpect(jsonPath("$[0].address").value("addr1"))
            .andExpect(jsonPath("$[0].imageUrl").value("img1"));
    }
    
    @TestMember
    @DisplayName("정보나눔 피드 목록을 파라미터와 함께 조회한다.")
    @Test
    void getInformationList_WithParams() throws Exception {
        // given
        Long lastId = 10L;
        String category = "RESTAURANT_CAFE";
        Boolean sortByRecent = false;
        
        var responses = List.of(
            createInformationListItem(4L, "title4", category, "addr4", "img4"),
            createInformationListItem(5L, "title5", category, "addr5", "img5")
        );
        given(informationService.getInformationList(lastId, category, sortByRecent)).willReturn(responses);
        
        // expected
        mockMvc.perform(
                get(BASE_URL)
                    .param("lastId", String.valueOf(lastId))
                    .param("category", category)
                    .param("sortByRecent", String.valueOf(sortByRecent))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(responses.size()))
            .andExpect(jsonPath("$[0].category").value(category))
            .andExpect(jsonPath("$[0].informationId").value(4));
    }
    
    private MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
            "images",
            "test-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
    }
    
    private InformationDetailResponse createInformationDetailResponse() {
        return InformationDetailResponse.builder()
            .informationId(1L)
            .title("title")
            .description("description")
            .category("HOSPITAL_FACILITIES")
            .address("address")
            .latitude(37.123456)
            .longitude(127.123456)
            .createdAt(LocalDate.now())
            .writer(createMemberDetailResponse())
            .images(List.of(createImageResponse(), createImageResponse()))
            .build();
    }
    
    private MemberDetailResponse createMemberDetailResponse() {
        return MemberDetailResponse.builder()
            .memberId(1L)
            .nickname("nickname")
            .profileImageUrl("profileImageURL")
            .role("ROLE_USER")
            .build();
    }
    
    private ImageResponse createImageResponse() {
        return ImageResponse.builder()
            .imageId(1L)
            .imageUrl("imageUrl")
            .build();
    }
    
    private InformationResponse createInformationListItem(Long id, String title, String category, String address, String imageUrl) {
        return InformationResponse.builder()
            .informationId(id)
            .title(title)
            .category(category)
            .address(address)
            .imageUrl(imageUrl)
            .build();
    }
    
}