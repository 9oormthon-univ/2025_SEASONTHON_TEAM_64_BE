package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.AlreadyUploadedTodayException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.TodayMissionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class MissionValidatorTest extends IntegrationTestSupport {
    
    @Autowired
    private MissionValidator missionValidator;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private MemberMissionRepository memberMissionRepository;
    
    @Autowired
    private FeedRepository feedRepository;
    
    @DisplayName("오늘 미션이 배정되어 있으면 예외가 발생하지 않는다.")
    @Test
    void isTodayMissionAssigned() {
        // given
        Member admin = createMember("admin3@test.com", "admin3", Role.ROLE_ADMIN);
        Member member = createMember("member3@test.com", "member3", Role.ROLE_USER);
        memberRepository.saveAll(List.of(admin, member));
        
        Mission mission = createMission(admin);
        missionRepository.save(mission);
        
        MemberMission memberMission = MemberMission.builder()
            .member(member)
            .mission(mission)
            .forDate(LocalDate.now())
            .build();
        memberMissionRepository.save(memberMission);
        
        // expected
        assertThatCode(() -> missionValidator.isTodayMissionAssigned(member.getId()))
            .doesNotThrowAnyException();
    }
    
    @DisplayName("오늘 미션이 배정되어 있지 않으면 예외가 발생한다.")
    @Test
    void isTodayMissionAssigned_WhenNotAssigned() {
        // given
        Member member = memberRepository.save(createMember("member4@test.com", "member4", Role.ROLE_USER));
        
        // expect
        assertThatCode(() -> missionValidator.isTodayMissionAssigned(member.getId()))
            .isInstanceOf(TodayMissionNotFoundException.class);
    }
    
    @DisplayName("오늘 미션으로 피드를 아직 생성하지 않았다면 예외가 발생하지 않는다.")
    @Test
    void isTodayFeedGenerated() {
        // given
        Member admin = createMember("admin5@test.com", "admin5", Role.ROLE_ADMIN);
        Member member = createMember("member5@test.com", "member5", Role.ROLE_USER);
        memberRepository.saveAll(List.of(admin, member));
        
        Mission mission = missionRepository.save(createMission(admin));
        
        // expected
        assertThatCode(() -> missionValidator.isTodayFeedGenerated(member.getId(), mission.getId()))
            .doesNotThrowAnyException();
    }
    
    @DisplayName("오늘 미션으로 이미 피드를 생성했다면 예외가 발생한다.")
    @Test
    void isTodayFeedGenerated_WhenAlreadyGenerated() {
        // given
        Member admin = createMember("admin6@test.com", "admin6", Role.ROLE_ADMIN);
        Member member = createMember("member6@test.com", "member6", Role.ROLE_USER);
        memberRepository.saveAll(List.of(admin, member));
        
        Mission mission = missionRepository.save(createMission(admin));
        Feed feed = Feed.builder()
            .member(member)
            .mission(mission)
            .description("설명")
            .imageUrl("http://example.com/feed.png")
            .build();
        feedRepository.save(feed);
        
        // expected
        assertThatCode(() -> missionValidator.isTodayFeedGenerated(member.getId(), mission.getId()))
            .isInstanceOf(AlreadyUploadedTodayException.class);
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
    
}

