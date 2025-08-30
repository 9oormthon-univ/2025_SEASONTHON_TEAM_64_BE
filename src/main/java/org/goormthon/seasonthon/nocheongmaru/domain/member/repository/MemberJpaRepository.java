package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByEmail(String email);
    
    boolean existsByIdAndRefreshToken(Long id, String refreshToken);
    
}
