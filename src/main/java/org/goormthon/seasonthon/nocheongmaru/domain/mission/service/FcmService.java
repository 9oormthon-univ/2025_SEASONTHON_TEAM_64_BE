package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import java.time.LocalDate;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

	public void sendTodayMission(Member member, Mission mission, LocalDate forDate) {
		if (member.getFcmToken() == null)
			return; // 토큰 없는 유저는 스킵

		String title = "오늘의 미션이 도착했어요!";
		String body = "[" + forDate + "] " + mission.getTitle();
	}
}