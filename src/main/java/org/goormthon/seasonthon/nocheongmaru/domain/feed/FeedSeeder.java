package org.goormthon.seasonthon.nocheongmaru.domain.feed;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.MissionJpaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedSeeder implements ApplicationRunner {

	private final FeedJpaRepository feedJpaRepository;
	private final MemberJpaRepository memberJpaRepository;
	private final MissionJpaRepository missionJpaRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// Member 데이터 생성
		Member member = memberJpaRepository.save(Member.builder()
			.email("testuser@example.com")
			.nickname("testuser")
			.profileImageURL("http://example.com/image.jpg")
			.role(Role.ROLE_USER)
			.build());

		// Mission 데이터 생성
		Mission mission = missionJpaRepository.save(Mission.builder()
			.title("Test Mission")
			.member(member)
			.build());

		// Feed 데이터 4개 생성
		for (int i = 1; i <= 4; i++) {
			Feed feed = Feed.builder()
				.description("Feed " + i + " description.")
				.imageUrl("http://example.com/feed" + i + ".jpg")
				.member(member)
				.mission(mission)
				.build();
			feedJpaRepository.save(feed);
		}

		System.out.println("4 Test Feed data inserted.");
	}
}
