package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentGeneratorTest extends IntegrationTestSupport {
    
    @Autowired
    private CommentGenerator commentGenerator;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        memberMissionRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("댓글을 생성한다.")
    @Test
    void generateComment() {
        // given
        Member admin = createMember(Role.ROLE_ADMIN, "admin@test.com");
        Member user = createMember(Role.ROLE_USER, "user@test.com");
        memberRepository.saveAll(List.of(admin, user));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        MemberMission memberMission = createMemberMission(user, mission);
        memberMissionRepository.save(memberMission);
        
        Feed feed = createFeed(user, mission);
        feedRepository.save(feed);
        
        // when
        Long commentId = commentGenerator.generateComment(user, feed, "댓글 내용");
        
        // then
        Comment comment = commentRepository.findById(commentId);
        assertThat(comment.getId()).isEqualTo(commentId);
    }
    
    private Member createMember(Role role, String email) {
        return Member.builder()
            .nickname("nickname")
            .email(email)
            .profileImageURL("profileImageUrl")
            .role(role)
            .build();
    }
    
    private Mission createMission(Member member) {
        return Mission.builder()
            .description("description")
            .member(member)
            .build();
    }
    
    private MemberMission createMemberMission(Member member, Mission mission) {
        LocalDate today = LocalDate.now();
        return MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(today)
            .build();
    }
    
    private Feed createFeed(Member member, Mission mission) {
        return Feed.builder()
            .member(member)
            .mission(mission)
            .description("description")
            .imageUrl("imageUrl")
            .build();
    }
    
}