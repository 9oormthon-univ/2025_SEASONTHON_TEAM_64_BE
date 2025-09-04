package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import java.time.LocalDate;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

	private final FirebaseMessaging firebaseMessaging;

	public void sendTodayMission(Member member, Mission mission, LocalDate forDate) {
		if (member.getFcmToken() == null) {
			log.warn("FCM 토큰이 없어 알림을 보낼 수 없습니다. memberId={}", member.getId());
			return;
		}

		String title = "오늘의 미션이 도착했어요!";
		String body = "[" + forDate + "] " + mission.getTitle();

		Message message = Message.builder()
			.setToken(member.getFcmToken())
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.putData("missionId", mission.getId().toString())
			.putData("date", forDate.toString())
			.build();

		try {
			String response = firebaseMessaging.send(message);
			log.info("FCM 발송 성공: memberId={}, response={}", member.getId(), response);
		} catch (FirebaseMessagingException e) {
			log.error("FCM 발송 실패: memberId={}, error={}", member.getId(), e.getMessage(), e);
		}
	}
}