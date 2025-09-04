package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.goormthon.seasonthon.nocheongmaru.domain.image.repository.ImageRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.InformationNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InformationReaderTest extends IntegrationTestSupport {
    
    @Autowired
    private InformationReader informationReader;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private InformationRepository informationRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        informationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("정보나눔 피드를 상세 조회한다.")
    @Test
    void getInformationDetail() {
        // given
        Member member = createMember("tester", "tester@example.com");
        memberRepository.save(member);
        
        Information information = createInformation(member);
        informationRepository.save(information);
        
        Image img1 = createImage("https://cdn.example.com/img1.png", information);
        Image img2 = createImage("https://cdn.example.com/img2.png", information);
        imageRepository.saveAll(List.of(img1, img2));
        
        // when
        InformationDetailResponse response = informationReader.getInformationDetail(information.getId());
        
        // then
        assertThat(response)
            .extracting(
                "informationId",
                "title",
                "description",
                "category",
                "address",
                "latitude",
                "longitude",
                "createdAt"
            )
            .containsExactly(
                information.getId(),
                information.getTitle(),
                information.getDescription(),
                information.getCategory().toString(),
                information.getAddress(),
                information.getLatitude(),
                information.getLongitude(),
                LocalDate.from(information.getCreatedAt())
            );
        
        assertThat(response.writer()).isNotNull();
        assertThat(response.images()).hasSize(2);
    }
    
    @DisplayName("존재하지 않는 정보나눔 피드 상세 조회 시 예외가 발생한다.")
    @Test
    void getInformationDetail_NotFound() {
        // when & then
        assertThatThrownBy(() -> informationReader.getInformationDetail(999999L))
            .isInstanceOf(InformationNotFoundException.class)
            .hasMessage("정보나눔 피드를 찾을 수 없습니다.");
    }
    
    @DisplayName("정보나눔 피드 목록을 최신순으로 기본 조회하면 8개가 id 내림차순으로 반환된다.")
    @Test
    void getInformationList_DefaultRecent_Returns8Desc() {
        // given
        Member member = createMember("tester", "tester@example.com");
        memberRepository.save(member);
        
        List<Long> savedIds = IntStream.rangeClosed(1, 12)
            .mapToObj(i -> {
                Category cat = (i % 2 == 0) ? Category.HOSPITAL_FACILITIES : Category.RESTAURANT_CAFE;
                Information info = createInformation(member, cat);
                informationRepository.save(info);
                return info.getId();
            })
            .toList();
        
        // when
        List<InformationResponse> result = informationReader.getInformationList(null, null, null);
        
        // then
        assertThat(result).hasSize(8);
    }
    
    @DisplayName("정보나눔 피드 목록을 카테고리로 필터링하면 해당 카테고리만 반환된다.")
    @Test
    void getInformationList_FilterByCategory() {
        // given
        Member member = createMember("tester", "tester@example.com");
        memberRepository.save(member);
        
        List<Information> infos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            infos.add(createInformation(member, Category.HOSPITAL_FACILITIES));
        }
        for (int i = 0; i < 5; i++) {
            infos.add(createInformation(member, Category.RESTAURANT_CAFE));
        }
        infos.forEach(informationRepository::save);
        
        // when
        List<InformationResponse> result = informationReader.getInformationList(null, "RESTAURANT_CAFE", true);
        
        // then
        assertThat(result).hasSize(5);
        assertThat(result).allMatch(r -> r.category().equals("RESTAURANT_CAFE"));
    }
    
    @DisplayName("정보나눔 피드 목록을 최신순으로 조회 시 lastId를 기준으로 다음 페이지를 반환한다.")
    @Test
    void getInformationList_Pagination_WithLastId_Recent() {
        // given
        Member member = createMember("tester", "tester@example.com");
        memberRepository.save(member);
        
        List<Long> ids = IntStream.rangeClosed(1, 12)
            .mapToObj(i -> {
                Information info = createInformation(member, Category.ETC);
                informationRepository.save(info);
                return info.getId();
            })
            .toList();
        
        List<Long> firstPageDesc = ids.stream().sorted(Comparator.reverseOrder()).limit(8).toList();
        Long lastId = firstPageDesc.getLast();
        
        // when
        List<InformationResponse> nextPage = informationReader.getInformationList(lastId, null, true);
        
        // then
        List<Long> expected = ids.stream()
            .filter(id -> id < lastId)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        List<Long> actual = nextPage.stream().map(InformationResponse::informationId).collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expected);
    }
    
    @DisplayName("정보나눔 피드 목록을 오래된순으로 조회하면 id 오름차순으로 8개가 반환된다.")
    @Test
    void getInformationList_SortByOldest_Returns8Asc() {
        // given
        Member member = createMember("tester", "tester@example.com");
        memberRepository.save(member);
        
        List<Long> savedIds = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> {
                Information info = createInformation(member, Category.HOSPITAL_FACILITIES);
                informationRepository.save(info);
                return info.getId();
            })
            .toList();
        
        // when
        List<InformationResponse> result = informationReader.getInformationList(null, null, false);
        
        // then
        assertThat(result).hasSize(8);
        List<Long> expectedAsc = savedIds.stream().sorted().limit(8).collect(Collectors.toList());
        List<Long> actualAsc = result.stream().map(InformationResponse::informationId).collect(Collectors.toList());
        assertThat(actualAsc).containsExactlyElementsOf(expectedAsc);
    }
    
    private Member createMember(String nickname, String email) {
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .profileImageURL("profileImageURL")
            .role(Role.ROLE_USER)
            .build();
    }
    
    private Information createInformation(Member member) {
        return Information.builder()
            .member(member)
            .title("title")
            .description("description")
            .address("address")
            .category(Category.HOSPITAL_FACILITIES)
            .latitude(37.123456)
            .longitude(127.123456)
            .build();
    }
    
    private Information createInformation(Member member, Category category) {
        return Information.builder()
            .member(member)
            .title("title")
            .description("description")
            .address("address")
            .category(category)
            .latitude(37.123456)
            .longitude(127.123456)
            .build();
    }
    
    private Image createImage(String url, Information information) {
        return Image.builder()
            .imageUrl(url)
            .information(information)
            .build();
    }
    
}