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
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.CommentNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.IsNotCommentInFeedException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.comment.IsNotCommentOwnerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentEditorTest extends IntegrationTestSupport {
    
    @Autowired
    private CommentEditor commentEditor;
    
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
    
    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() {
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
        
        Comment comment = Comment.builder()
            .member(user)
            .feed(feed)
            .description("description")
            .build();
        commentRepository.save(comment);
        
        // when
        commentEditor.deleteComment(comment.getId(), user.getId(), feed.getId());
        
        // then
        assertThatThrownBy(() -> commentRepository.findById(comment.getId()))
            .isInstanceOf(CommentNotFoundException.class);
    }
    
    @DisplayName("게시물에 속하지 않은 댓글에 접근할 경우, 예외가 발생한다.")
    @Test
    void deleteComment_NotInFeed() {
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
        
        Comment comment = Comment.builder()
            .member(user)
            .feed(feed)
            .description("description")
            .build();
        commentRepository.save(comment);
        
        // when & then
        assertThatThrownBy(() -> commentEditor.deleteComment(comment.getId(), user.getId(), feed.getId() + 1))
            .isInstanceOf(IsNotCommentInFeedException.class);
    }
    
    @DisplayName("댓글 작성자가 아닌 사용자가 댓글을 삭제할 경우, 예외가 발생한다.")
    @Test
    void deleteComment_NotOwner() {
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
        
        Comment comment = Comment.builder()
            .member(user)
            .feed(feed)
            .description("description")
            .build();
        commentRepository.save(comment);
        
        // when & then
        assertThatThrownBy(() -> commentEditor.deleteComment(comment.getId(), admin.getId(), feed.getId()))
            .isInstanceOf(IsNotCommentOwnerException.class);
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