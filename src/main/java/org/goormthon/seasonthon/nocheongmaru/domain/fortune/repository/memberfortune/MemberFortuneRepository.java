package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.MemberFortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberFortuneRepository {

    private final MemberFortuneJpaRepository memberFortuneJpaRepository;
    
    public void save(MemberFortune memberFortune) {
        memberFortuneJpaRepository.save(memberFortune);
    }
    
    public boolean existsAssignedToday(Member member) {
        return memberFortuneJpaRepository.existsByMemberAndAssignedAt(member, LocalDate.now());
    }
    
    public void deleteAllInBatch() {
        memberFortuneJpaRepository.deleteAllInBatch();
    }
    
    public Long count() {
        return memberFortuneJpaRepository.count();
    }
    
    public List<FortuneResponse> getMyFortunes(Long memberId) {
        return memberFortuneJpaRepository.getMyFortunes(memberId);
    }
    
}
