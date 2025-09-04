package org.goormthon.seasonthon.nocheongmaru.domain.notification.service;

import java.time.LocalDate;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
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

	public void sendNotification(Member member, String title, String body) {
		if (member.getFcmToken() == null) {
			log.warn("FCM 토큰 없음: memberId={}", member.getId());
			return;
		}

		Message message = Message.builder()
			.setToken(member.getFcmToken())
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();

		try {
			String response = firebaseMessaging.send(message);
			log.info("FCM 발송 성공: memberId={}, response={}", member.getId(), response);
		} catch (FirebaseMessagingException e) {
			log.error("FCM 발송 실패: memberId={}, error={}", member.getId(), e.getMessage(), e);
		}
	}

	public void sendToTokens(List<String> tokens, String title, String body) {
		if (tokens == null || tokens.isEmpty()) return;

		MulticastMessage message = MulticastMessage.builder()
			.addAllTokens(tokens)
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();

		try {
			BatchResponse response = firebaseMessaging.sendMulticast(message);
			log.info("멀티 FCM 발송 완료: 성공={} 실패={}", response.getSuccessCount(), response.getFailureCount());
		} catch (FirebaseMessagingException e) {
			log.error("멀티 FCM 발송 실패: {}", e.getMessage(), e);
		}
	}

}