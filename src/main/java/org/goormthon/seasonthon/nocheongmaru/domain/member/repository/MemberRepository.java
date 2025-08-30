package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    
    private final MemberJpaRepository memberJpaRepository;
    
}
