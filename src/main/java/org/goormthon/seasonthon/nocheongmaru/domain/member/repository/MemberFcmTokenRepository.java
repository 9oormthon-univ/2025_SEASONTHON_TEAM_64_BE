package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.MemberFcmToken;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberFcmTokenRepository {

	private final MemberFcmTokenJpaRepository jpaRepository;

	public boolean existsByToken(String token) {
		return jpaRepository.existsByToken(token);
	}

	public Optional<MemberFcmToken> findByToken(String token) {
		return jpaRepository.findByToken(token);
	}

	public void deleteByToken(String token) {
		jpaRepository.deleteByToken(token);
	}

	public List<MemberFcmToken> findAllByMemberId(Long memberId) {
		return jpaRepository.findAllByMemberId(memberId);
	}

	public MemberFcmToken save(MemberFcmToken entity) {
		return jpaRepository.save(entity);
	}
}
