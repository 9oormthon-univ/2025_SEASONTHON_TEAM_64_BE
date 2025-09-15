package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.InMemoryPushSender;
import org.goormthon.seasonthon.nocheongmaru.SingleRecord;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.TestNotificationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestNotificationConfig.class)
class FeedLikeNotificationTest extends IntegrationTestSupport {
    
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
    
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    
    @Autowired
    private InMemoryPushSender pushSender;
    
    @AfterEach
    void tearDown() {
        pushSender.clear();
        notificationJpaRepository.deleteAllInBatch();
        feedLikeRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("다른 사용자가 좋아요를 누르면 FCM 알림이 전송된다.")
    @Test
    void sendOnOthersLike() {
        // given
        Member admin = memberRepository.save(createMember("admin@l.com", "admin", Role.ROLE_ADMIN));
        Member owner = memberRepository.save(createMember("owner@l.com", "owner", Role.ROLE_USER));
        owner.updateDeviceToken("token-like-owner");
        memberRepository.save(owner);
        Member liker = memberRepository.save(createMember("liker@l.com", "liker", Role.ROLE_USER));
        
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = createFeed(owner, mission);
        feedRepository.save(feed);
        
        // when
        feedLikeService.like(liker.getId(), feed.getId());
        
        // then
        assertThat(pushSender.getSingles()).hasSize(1);
        SingleRecord rec = pushSender.getSingles().get(0);
        assertThat(rec.token()).isEqualTo("token-like-owner");
        assertThat(rec.data().get("type")).isEqualTo(NotificationType.LIKE.name());
        assertThat(rec.data().get("feedId")).isEqualTo(String.valueOf(feed.getId()));
        assertThat(rec.title()).isEqualTo("공감");
        assertThat(rec.body()).contains("liker");
        assertThat(notificationJpaRepository.count()).isEqualTo(1);
    }
    
    @DisplayName("본인 피드에 좋아요를 눌러도 알림이 전송되지 않는다.")
    @Test
    void notSendOnSelfLike() {
        // given
        Member admin = memberRepository.save(createMember("admin@sl.com", "admin", Role.ROLE_ADMIN));
        Member owner = memberRepository.save(createMember("owner@sl.com", "owner", Role.ROLE_USER));
        owner.updateDeviceToken("token-like-owner2");
        memberRepository.save(owner);
        
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = createFeed(owner, mission);
        feedRepository.save(feed);
        
        // when
        feedLikeService.like(owner.getId(), feed.getId());
        
        // then
        assertThat(pushSender.getSingles()).isEmpty();
        assertThat(notificationJpaRepository.count()).isEqualTo(0);
    }
    
    @DisplayName("같은 사용자가 같은 피드를 두 번 좋아요 눌러도 알림은 1회만 전송된다.")
    @Test
    void notDuplicateNotificationOnDuplicateLike() {
        // given
        Member admin = memberRepository.save(createMember("admin@dl.com", "admin", Role.ROLE_ADMIN));
        Member owner = memberRepository.save(createMember("owner@dl.com", "owner", Role.ROLE_USER));
        owner.updateDeviceToken("token-like-owner3");
        memberRepository.save(owner);
        Member liker = memberRepository.save(createMember("liker@dl.com", "liker", Role.ROLE_USER));
        
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = createFeed(owner, mission);
        feedRepository.save(feed);
        
        // when
        feedLikeService.like(liker.getId(), feed.getId());
        feedLikeService.like(liker.getId(), feed.getId());
        
        // then
        assertThat(pushSender.getSingles()).hasSize(1);
        assertThat(notificationJpaRepository.count()).isEqualTo(1);
    }
    
    private Member createMember(String email, String nickname, Role role) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL("profile")
            .role(role)
            .build();
    }
    
    private Mission createMission(Member member) {
        return Mission.builder()
            .description("desc")
            .member(member)
            .build();
    }
    
    private Feed createFeed(Member member, Mission mission) {
        return Feed.builder()
            .member(member)
            .mission(mission)
            .description("d")
            .imageUrl("img")
            .build();
    }
}
