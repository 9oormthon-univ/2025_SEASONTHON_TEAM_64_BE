package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.MissionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionAssignmentService {

	private final MissionRepository missionRepo;
	private final MemberRepository memberRepo;
	private final MemberMissionRepository memberMissionRepo;
	private final FcmService fcmService;

	@Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
	public void assignDailyAndNotify() {
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		List<Mission> pool = missionRepo.findAllForAssignment();
		if (pool.isEmpty()) return;

		List<Member> members = memberRepo.findAll();
		for (Member member : members) {
			if (memberMissionRepo.existsByMemberIdAndForDate(member.getId(), today)) continue;

			int offset = Math.toIntExact(Math.floorMod(member.getId(), pool.size()));
			Mission assigned = pickNonDuplicated(pool, offset, member.getId());

			MemberMission mm = MemberMission.builder()
				.member(member).mission(assigned)
				.forDate(today).status(MemberMission.Status.ASSIGNED)
				.build();
			memberMissionRepo.save(mm);

			fcmService.sendTodayMission(member, assigned, today);
		}
	}

	private Mission pickNonDuplicated(List<Mission> pool, int offset, Long memberId) {
		int n = pool.size();
		for (int i = 0; i < n; i++) {
			Mission cand = pool.get((offset + i) % n);
			if (!memberMissionRepo.existsByMemberIdAndMissionId(memberId, cand.getId())) return cand;
		}
		return pool.get(offset % n);
	}
}
