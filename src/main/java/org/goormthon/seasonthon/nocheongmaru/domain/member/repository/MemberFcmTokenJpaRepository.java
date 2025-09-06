package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.MemberFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFcmTokenJpaRepository extends JpaRepository<MemberFcmToken, Long> {

	// 특정 토큰 존재 여부 확인
	boolean existsByToken(String token);

	// 토큰으로 엔티티 찾기
	Optional<MemberFcmToken> findByToken(String token);

	// 토큰 삭제
	void deleteByToken(String token);

	// 회원이 가진 모든 토큰 가져오기
	List<MemberFcmToken> findAllByMemberId(Long memberId);
}
