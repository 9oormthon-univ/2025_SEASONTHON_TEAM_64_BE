package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.InMemoryPushSender;
import org.goormthon.seasonthon.nocheongmaru.SingleRecord;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.TestNotificationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestNotificationConfig.class)
class CommentNotificationTest extends IntegrationTestSupport {
    
    @Autowired
    private CommentGenerator commentGenerator;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    
    @Autowired
    private InMemoryPushSender pushSender;
    
    @AfterEach
    void tearDown() {
        pushSender.clear();
        notificationJpaRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("다른 사용자가 댓글을 달면 FCM 알림이 전송된다.")
    @Test
    void sendOnOthersComment() {
        // given
        Member admin = memberRepository.save(createMember("admin@c.com", "admin", Role.ROLE_ADMIN));
        Member owner = memberRepository.save(createMember("owner@c.com", "owner", Role.ROLE_USER));
        owner.updateDeviceToken("token-owner");
        memberRepository.save(owner);
        Member commenter = memberRepository.save(createMember("commenter@c.com", "commenter", Role.ROLE_USER));
        
        Mission mission = missionRepository.save(createMission(admin));
        MemberMission mm = memberMissionRepository.save(createMemberMission(owner, mission));
        Feed feed = createFeed(owner, mission);
        feedRepository.save(feed);
        
        // when
        commentGenerator.generateComment(commenter, feed, "댓글 내용");
        
        // then
        assertThat(pushSender.getSingles()).hasSize(1);
        SingleRecord rec = pushSender.getSingles().get(0);
        assertThat(rec.token()).isEqualTo("token-owner");
        assertThat(rec.data().get("type")).isEqualTo(NotificationType.COMMENT.name());
        assertThat(rec.data().get("feedId")).isEqualTo(String.valueOf(feed.getId()));
        assertThat(rec.title()).isEqualTo("댓글");
        assertThat(rec.body()).contains("commenter");
        assertThat(notificationJpaRepository.count()).isEqualTo(1);
    }
    
    @DisplayName("본인 피드에 댓글을 달면 알림이 전송되지 않는다.")
    @Test
    void notSendOnSelfComment() {
        // given
        Member admin = memberRepository.save(createMember("admin@sc.com", "admin", Role.ROLE_ADMIN));
        Member owner = memberRepository.save(createMember("owner@sc.com", "owner", Role.ROLE_USER));
        owner.updateDeviceToken("token-owner2");
        memberRepository.save(owner);
        
        Mission mission = missionRepository.save(createMission(admin));
        memberMissionRepository.save(createMemberMission(owner, mission));
        Feed feed = createFeed(owner, mission);
        feedRepository.save(feed);
        
        // when
        commentGenerator.generateComment(owner, feed, "내가 단 댓글");
        
        // then
        assertThat(pushSender.getSingles()).isEmpty();
        assertThat(notificationJpaRepository.count()).isEqualTo(0);
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
    
    private MemberMission createMemberMission(Member member, Mission mission) {
        return MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(LocalDate.now())
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
