package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.FeedNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.IsNotFeedOwnerException;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class FeedEditorTest extends IntegrationTestSupport {
    
    @Autowired
    private FeedEditor feedEditor;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @MockitoBean
    private S3StorageUtil s3StorageUtil;
    
    @DisplayName("피드를 수정한다.")
    @Test
    void modifyFeed() {
        // given
        Member owner = memberRepository.save(createMember("owner-fe@test.com", "owner-fe", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin-fe@test.com", "admin-fe", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("old desc")
            .imageUrl("http://example.com/old-fe.png")
            .build();
        feedRepository.save(feed);
        
        MockMultipartFile newImage = createMockFile("new-image-fe", "new-image-fe");
        given(s3StorageUtil.uploadFileToS3(newImage)).willReturn("http://example.com/new-fe.png");
        
        // when
        feedEditor.modifyFeed(owner, feed.getId(), "new desc", newImage);
        
        // then
        Feed updated = feedRepository.findById(feed.getId());
        assertThat(updated.getDescription()).isEqualTo("new desc");
        assertThat(updated.getImageUrl()).isEqualTo("http://example.com/new-fe.png");
        verify(s3StorageUtil).deleteFileFromS3("http://example.com/old-fe.png");
        verify(s3StorageUtil).uploadFileToS3(newImage);
    }
    
    @DisplayName("피드를 수정할 때 소유자가 아니면 예외가 발생한다.")
    @Test
    void modifyFeed_NotOwner() {
        // given
        Member owner = memberRepository.save(createMember("owner2-fe@test.com", "owner2-fe", Role.ROLE_USER));
        Member other = memberRepository.save(createMember("other2-fe@test.com", "other2-fe", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin2-fe@test.com", "admin2-fe", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("old desc")
            .imageUrl("http://example.com/old2-fe.png")
            .build();
        feedRepository.save(feed);
        
        MockMultipartFile newImage = createMockFile("new-image2-fe", "new-image2-fe");
        
        // expect
        assertThatThrownBy(() -> feedEditor.modifyFeed(other, feed.getId(), "new desc", newImage))
            .isInstanceOf(IsNotFeedOwnerException.class);
        verify(s3StorageUtil, never()).deleteFileFromS3(anyString());
        verify(s3StorageUtil, never()).uploadFileToS3(any());
    }
    
    @DisplayName("피드를 삭제한다.")
    @Test
    void deleteFeed() {
        // given
        Member owner = memberRepository.save(createMember("owner3-fe@test.com", "owner3-fe", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin3-fe@test.com", "admin3-fe", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/to-delete-fe.png")
            .build();
        feedRepository.save(feed);
        Long feedId = feed.getId();
        
        // when
        feedEditor.deleteFeed(owner, feedId);
        
        // then
        verify(s3StorageUtil).deleteFileFromS3("http://example.com/to-delete-fe.png");
        assertThat(org.assertj.core.api.Assertions.catchThrowable(() -> feedRepository.findById(feedId)))
            .isInstanceOf(FeedNotFoundException.class);
    }
    
    @DisplayName("피드를 삭제할 때 소유자가 아니면 예외가 발생한다.")
    @Test
    void deleteFeed_NotOwner() {
        // given
        Member owner = memberRepository.save(createMember("owner4-fe@test.com", "owner4-fe", Role.ROLE_USER));
        Member other = memberRepository.save(createMember("other4-fe@test.com", "other4-fe", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin4-fe@test.com", "admin4-fe", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = Feed.builder()
            .member(owner)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/not-owner-fe.png")
            .build();
        feedRepository.save(feed);
        Long feedId = feed.getId();
        
        // expect
        assertThatThrownBy(() -> feedEditor.deleteFeed(other, feedId))
            .isInstanceOf(IsNotFeedOwnerException.class);
        verify(s3StorageUtil, never()).deleteFileFromS3(anyString());
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

