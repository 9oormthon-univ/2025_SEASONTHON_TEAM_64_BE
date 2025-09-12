package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class FeedGeneratorTest extends IntegrationTestSupport {
    
    @Autowired
    private FeedGenerator feedGenerator;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @MockitoBean
    private S3StorageUtil s3StorageUtil;
    
    @DisplayName("오늘의 시선 피드를 작성한다.")
    @Test
    void generateFeed() {
        // given
        Member admin = createMember("admin@test.com", "admin", Role.ROLE_ADMIN);
        Member member = createMember("member@test.com", "member", Role.ROLE_USER);
        memberRepository.saveAll(List.of(
            admin,
            member
        ));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        MockMultipartFile image = createMockFile("image1", "image1");
        given(s3StorageUtil.uploadFileToS3(image))
            .willReturn("http://example.com/image1.png");
        
        // when
        Long feedId = feedGenerator.generateFeed(member, mission, "피드 설명", image);
        
        // then
        Feed feed = feedRepository.findById(feedId);
        assertThat(feed.getId()).isEqualTo(feedId);
    }
    
    private static Mission createMission(Member member) {
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