package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    
    private final MemberJpaRepository memberJpaRepository;
    
    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);
    }
    
    public void save(Member newMember) {
        memberJpaRepository.save(newMember);
    }
    
    public Member findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
    }
    
    public boolean existsByIdAndRefreshToken(Long memberId, String refreshToken) {
        return memberJpaRepository.existsByIdAndRefreshToken(memberId, refreshToken);
    }
    
    public void deleteAllInBatch() {
        memberJpaRepository.deleteAllInBatch();
    }

    public boolean existsById(Long memberId) {
        return memberJpaRepository.existsById(memberId);
    }

    public void saveAll(List<Member> members) {
        memberJpaRepository.saveAll(members);
    }

}
