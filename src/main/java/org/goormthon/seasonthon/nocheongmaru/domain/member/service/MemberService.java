package org.goormthon.seasonthon.nocheongmaru.domain.member.service;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public void updateFcmToken(Long memberId, String token) {
		if (token == null || token.isBlank()) throw new IllegalArgumentException("token blank");
		Member m = memberRepository.findById(memberId);
		m.updateFcmToken(token);
	}
}
