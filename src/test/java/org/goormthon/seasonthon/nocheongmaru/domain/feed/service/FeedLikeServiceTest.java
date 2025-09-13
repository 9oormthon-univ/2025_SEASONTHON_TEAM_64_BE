package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class FeedLikeServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private FeedLikeService feedLikeService;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @Autowired
    private FeedLikeRepository feedLikeRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @AfterEach
    void tearDown() {
        feedLikeRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("같은 사용자가 같은 피드를 여러 번 좋아요해도 1건만 저장된다.")
    @Test
    void like() {
        // given
        Member member = memberRepository.save(createMember("like@test.com", "like", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin-like@test.com", "admin-like", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = Feed.builder()
            .member(member)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/img.png")
            .build();
        feedRepository.save(feed);
        
        // when
        feedLikeService.like(member.getId(), feed.getId());
        feedLikeService.like(member.getId(), feed.getId());
        
        // then
        assertThat(feedLikeRepository.existsByFeedIdAndMemberId(feed.getId(), member.getId())).isTrue();
        assertThat(feedLikeRepository.countByFeedIdAndMemberId(feed.getId(), member.getId())).isEqualTo(1);
    }
    
    @DisplayName("동시에 여러 요청이 좋아요를 시도해도 단 한 건만 저장된다.")
    @Test
    void like_Concurrent() throws InterruptedException {
        // given
        Member member = memberRepository.save(createMember("con@test.com", "con", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin-con@test.com", "admin-con", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = Feed.builder()
            .member(member)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/img.png")
            .build();
        feedRepository.save(feed);
        
        int threads = 20;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        ExecutorService es = Executors.newFixedThreadPool(threads);
        
        for (int i = 0; i < threads; i++) {
            es.submit(() -> {
                try {
                    start.await();
                    feedLikeService.like(member.getId(), feed.getId());
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }
        
        start.countDown();
        done.await(5, TimeUnit.SECONDS);
        es.shutdownNow();
        
        // then
        assertThat(feedLikeRepository.existsByFeedIdAndMemberId(feed.getId(), member.getId())).isTrue();
        assertThat(feedLikeRepository.countByFeedIdAndMemberId(feed.getId(), member.getId())).isEqualTo(1);
    }
    
    @DisplayName("좋아요를 취소하면 삭제된다.")
    @Test
    void unlike() {
        // given
        Member member = memberRepository.save(createMember("unlike@test.com", "unlike", Role.ROLE_USER));
        Member admin = memberRepository.save(createMember("admin-unlike@test.com", "admin-unlike", Role.ROLE_ADMIN));
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = Feed.builder()
            .member(member)
            .mission(mission)
            .description("desc")
            .imageUrl("http://example.com/img.png")
            .build();
        feedRepository.save(feed);
        
        feedLikeService.like(member.getId(), feed.getId());
        assertThat(feedLikeRepository.existsByFeedIdAndMemberId(feed.getId(), member.getId())).isTrue();
        
        // when
        feedLikeService.unlike(member.getId(), feed.getId());
        feedLikeService.unlike(member.getId(), feed.getId());
        
        // then
        assertThat(feedLikeRepository.existsByFeedIdAndMemberId(feed.getId(), member.getId())).isFalse();
        assertThat(feedLikeRepository.countByFeedIdAndMemberId(feed.getId(), member.getId())).isEqualTo(0);
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
}

