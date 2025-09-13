package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FeedReaderTest extends IntegrationTestSupport {
    
    @Autowired
    private FeedReader feedReader;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @Autowired
    private FeedLikeRepository feedLikeRepository;
    
    @AfterEach
    void tearDown() {
        feedLikeRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("마지막 ID가 null이 아닐 때, 피드 목록을 조회한다.")
    @Test
    void getFeeds_WithLastFeedId() {
        // given
        Member viewer = memberRepository.save(createMember("viewer2@test.com", "viewer2", Role.ROLE_USER));
        Member other = memberRepository.save(createMember("other2@test.com", "other2", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin2@test.com", "admin2", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Member owner = (i % 2 == 0) ? viewer : other;
            Feed feed = createFeed(owner, mission, "p2-desc-" + i, "http://example.com/p2-img-" + i + ".png");
            feedRepository.save(feed);
            ids.add(feed.getId());
        }
        Long maxId = ids.getLast();
        
        // when
        List<FeedResponse> responses = feedReader.getFeeds(viewer.getId(), maxId);
        
        // then
        assertThat(responses).hasSize(8);
        assertThat(responses.getFirst().feedId()).isEqualTo(ids.get(10));
    }
    
    @DisplayName("피드를 상세 조회한다.")
    @Test
    void getFeed() {
        // given
        Member viewer = memberRepository.save(createMember("viewer3@test.com", "viewer3", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin3@test.com", "admin3", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        
        Feed feed = createFeed(viewer, mission, "detail-desc", "http://example.com/detail.png");
        feedRepository.save(feed);
        feedLikeRepository.save(
            FeedLike.builder()
                .feed(feed)
                .member(viewer)
                .build()
        );
        
        // when
        FeedResponse response = feedReader.getFeed(viewer.getId(), feed.getId());
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.isMine()).isTrue();
        assertThat(Boolean.TRUE.equals(response.isLiked())).isTrue();
    }
    
    private Member createMember(String email, String nickname, Role role) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL("http://example.com/profile.jpg")
            .role(role)
            .build();
    }
    
    private Mission createMission(Member member) {
        return Mission.builder()
            .description("미션 설명")
            .member(member)
            .build();
    }
    
    private Feed createFeed(Member member, Mission mission, String description, String imageUrl) {
        return Feed.builder()
            .member(member)
            .mission(mission)
            .description(description)
            .imageUrl(imageUrl)
            .build();
    }
    
}