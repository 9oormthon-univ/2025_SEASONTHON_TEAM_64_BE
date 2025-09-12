package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request.FeedCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.request.FeedModifyServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.FeedNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class FeedServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private FeedService feedService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @MockitoBean
    private S3StorageUtil s3StorageUtil;
    
    @DisplayName("오늘의 시선 피드를 서비스로 생성한다.")
    @Test
    void generateFeed() {
        // given
        Member admin = createMember("admin2@test.com", "admin2", Role.ROLE_ADMIN);
        Member member = createMember("member2@test.com", "member2", Role.ROLE_USER);
        memberRepository.saveAll(List.of(admin, member));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        // 오늘 미션 배정
        MemberMission memberMission = MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(LocalDate.now())
            .build();
        memberMissionRepository.save(memberMission);
        
        MockMultipartFile image = createMockFile("image-feed-service", "image-feed-service");
        given(s3StorageUtil.uploadFileToS3(image))
            .willReturn("http://example.com/image-feed-service.png");
        
        FeedCreateServiceRequest request = FeedCreateServiceRequest.builder()
            .missionId(mission.getId())
            .memberId(member.getId())
            .description("서비스를 통한 피드 설명")
            .imageFile(image)
            .build();
        
        // when
        Long feedId = feedService.generateFeed(request);
        
        // then
        Feed saved = feedRepository.findById(feedId);
        assertThat(saved.getId()).isEqualTo(feedId);
        assertThat(saved.getMember().getId()).isEqualTo(member.getId());
        assertThat(saved.getMission().getId()).isEqualTo(mission.getId());
    }
    
    @DisplayName("피드를 수정한다.")
    @Test
    void modifyFeed() {
        // given
        Member owner = memberRepository.save(createMember("owner@test.com", "owner", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin@test.com", "admin", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("old desc")
            .imageUrl("http://example.com/old.png")
            .build();
        feedRepository.save(feed);
        
        MockMultipartFile newImage = createMockFile("new-image", "new-image");
        given(s3StorageUtil.uploadFileToS3(newImage))
            .willReturn("http://example.com/new.png");
        
        FeedModifyServiceRequest request = FeedModifyServiceRequest.builder()
            .memberId(owner.getId())
            .feedId(feed.getId())
            .description("new desc")
            .imageFile(newImage)
            .build();
        
        // when
        feedService.modifyFeed(request);
        
        // then
        Feed updated = feedRepository.findById(feed.getId());
        assertThat(updated.getDescription()).isEqualTo("new desc");
        assertThat(updated.getImageUrl()).isEqualTo("http://example.com/new.png");
        verify(s3StorageUtil).deleteFileFromS3("http://example.com/old.png");
        verify(s3StorageUtil).uploadFileToS3(newImage);
    }
    
    @DisplayName("피드를 삭제한다")
    @Test
    void deleteFeed() {
        // given
        Member owner = memberRepository.save(createMember("owner3@test.com", "owner3", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin3@test.com", "admin3", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/to-delete.png")
            .build();
        feedRepository.save(feed);
        Long feedId = feed.getId();
        
        // when
        feedService.deleteFeed(owner.getId(), feedId);
        
        // then
        verify(s3StorageUtil).deleteFileFromS3("http://example.com/to-delete.png");
        assertThatThrownBy(() -> feedRepository.findById(feedId))
            .isInstanceOf(FeedNotFoundException.class);
    }
    
    private Mission createMission(Member member) {
        return Mission.builder()
            .description("미션 설명")
            .member(member)
            .build();
    }
    
    private Member createMember(String email, String nickname, Role role) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL("http://example.com/profile.jpg")
            .role(role)
            .build();
    }
    
    private MockMultipartFile createMockFile(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename + ".png", MediaType.IMAGE_PNG_VALUE, name.getBytes());
    }
}
