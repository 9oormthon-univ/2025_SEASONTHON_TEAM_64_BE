package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CommentReaderTest extends IntegrationTestSupport {
    
    @Autowired
    private CommentReader commentReader;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    
    @DisplayName("댓글 목록을 기본 조회하면 특정 피드의 댓글이 최신순으로 8개 반환된다.")
    @Test
    void getCommentsByFeedId_Default_Returns8Desc() {
        // given
        Member viewer = createMember("viewer@test.com", "viewer", Role.ROLE_USER);
        Member other = createMember("other@test.com", "other", Role.ROLE_USER);
        Member admin = createMember("admin@test.com", "admin", Role.ROLE_ADMIN);
        memberRepository.saveAll(List.of(viewer, other, admin));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        Feed feed = createFeed(viewer, mission);
        feedRepository.save(feed);
        
        List<Long> commentIds = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Member writer = (i % 2 == 0) ? viewer : other;
            Comment c = createComment(writer, feed, "comment-" + i);
            commentRepository.save(c);
            commentIds.add(c.getId());
        }
        
        // when
        List<CommentResponse> result = commentReader.getCommentsByFeedId(feed.getId(), viewer.getId(), null);
        
        // then
        assertThat(result).hasSize(8);
    }
    
    @DisplayName("lastCommentId를 주면 그보다 작은 댓글 id 기준으로 다음 페이지가 반환된다.")
    @Test
    void getCommentsByFeedId_WithLastCommentId_Pagination() {
        // given
        Member viewer = createMember("viewer@test.com", "viewer", Role.ROLE_USER);
        Member other = createMember("other@test.com", "other", Role.ROLE_USER);
        Member admin = createMember("admin@test.com", "admin", Role.ROLE_ADMIN);
        memberRepository.saveAll(List.of(viewer, other, admin));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        Feed feed = createFeed(other, mission);
        feedRepository.save(feed);
        
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Member writer = (i % 2 == 0) ? viewer : other;
            Comment c = createComment(writer, feed, "c-" + i);
            commentRepository.save(c);
            ids.add(c.getId());
        }
        
        List<Long> firstPageDesc = ids.stream().sorted(Comparator.reverseOrder()).limit(8).toList();
        Long lastId = firstPageDesc.getLast();
        
        // when
        List<CommentResponse> nextPage = commentReader.getCommentsByFeedId(feed.getId(), viewer.getId(), lastId);
        
        // then
        List<Long> expected = ids.stream()
            .filter(id -> id < lastId)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        List<Long> actual = nextPage.stream().map(CommentResponse::commentId).collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expected);
    }
    
    @DisplayName("isMine은 조회자와 댓글 작성자가 동일하면 true이다.")
    @Test
    void getCommentsByFeedId_IsMineFlag() {
        // given
        Member viewer = createMember("viewer@test.com", "viewer", Role.ROLE_USER);
        Member other = createMember("other@test.com", "other", Role.ROLE_USER);
        Member admin = createMember("admin@test.com", "admin", Role.ROLE_ADMIN);
        memberRepository.saveAll(List.of(viewer, other, admin));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        Feed feed = createFeed(viewer, mission);
        feedRepository.save(feed);
        
        Comment mine = createComment(viewer, feed, "mine");
        commentRepository.save(mine);
        
        Comment notMine = createComment(other, feed, "not-mine");
        commentRepository.save(notMine);
        
        // when
        List<CommentResponse> result = commentReader.getCommentsByFeedId(feed.getId(), viewer.getId(), null);
        
        // then
        assertThat(result).isNotEmpty();
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
    
    private Feed createFeed(Member member, Mission mission) {
        return Feed.builder()
            .member(member)
            .mission(mission)
            .description("feed-desc")
            .imageUrl("http://example.com/image.png")
            .build();
    }
    
    private Comment createComment(Member member, Feed feed, String description) {
        return Comment.builder()
            .member(member)
            .feed(feed)
            .description(description)
            .build();
    }
}

