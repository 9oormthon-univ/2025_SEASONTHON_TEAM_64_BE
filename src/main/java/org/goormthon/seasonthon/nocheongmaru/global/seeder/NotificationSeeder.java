package org.goormthon.seasonthon.nocheongmaru.global.seeder;

import java.time.LocalDateTime;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.NotificationType;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class NotificationSeeder {

	@Bean
	@Transactional
	CommandLineRunner seedNotifications(MemberRepository memberRepo,
		NotificationRepository notificationRepo) {
		return args -> {
			// ✅ 알림 받을 사용자 생성
			Member user = memberRepo.save(Member.builder()
				.email("notify123@test.com")
				.nickname("알림유저")
				.role(Role.ROLE_USER)
				.profileImageURL("https://picsum.photos/70")
				.build());

			// ✅ 미션 알림
			notificationRepo.save(Notification.builder()
				.member(user)
				.message("새로운 미션이 도착했습니다.")
				.type(NotificationType.MISSION)
				.isRead(false)
				.createdAt(LocalDateTime.now())
				.build());

			// ✅ 댓글 알림
			notificationRepo.save(Notification.builder()
				.member(user)
				.message("피드에 새로운 댓글이 달렸습니다.")
				.type(NotificationType.COMMENT)
				.isRead(false)
				.createdAt(LocalDateTime.now())
				.build());

			// ✅ 좋아요 알림
			notificationRepo.save(Notification.builder()
				.member(user)
				.message("회원님의 피드에 좋아요가 눌렸습니다.")
				.type(NotificationType.LIKE)
				.isRead(false)
				.createdAt(LocalDateTime.now())
				.build());

			System.out.println("✅ Notification seeding 완료: userId=" + user.getId());
		};
	}
}
