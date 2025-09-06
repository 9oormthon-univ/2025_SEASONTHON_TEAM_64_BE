package org.goormthon.seasonthon.nocheongmaru.domain.member.service;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.MemberFcmToken;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberFcmTokenRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final MemberFcmTokenRepository tokenRepository;

	@Transactional
	public void updateFcmToken(Long memberId, String token) {
		Member member = memberRepository.findById(memberId);

		// 이미 등록된 토큰이면 무시
		if (tokenRepository.existsByToken(token)) return;

		// 새로운 토큰 저장
		MemberFcmToken newToken = MemberFcmToken.builder()
			.member(member)
			.token(token)
			.build();

		tokenRepository.save(newToken);
	}

	@Transactional
	public void removeInvalidToken(String token) {
		tokenRepository.deleteByToken(token);
	}

	@Transactional(readOnly = true)
	public Member getById(Long memberId) {
		return memberRepository.findById(memberId);
	}

}