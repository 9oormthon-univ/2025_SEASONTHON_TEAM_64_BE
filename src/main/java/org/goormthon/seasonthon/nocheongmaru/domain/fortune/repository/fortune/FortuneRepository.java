package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.exception.fortune.FortuneNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FortuneRepository {

    private final FortuneJpaRepository fortuneJpaRepository;
    
    
    public void save(Fortune fortune) {
        fortuneJpaRepository.save(fortune);
    }
    
    public boolean existsBySenderAndForDate(Member member) {
        LocalDate today = LocalDate.now();
        return fortuneJpaRepository.existsBySenderAndForDate(member, today);
    }
    
    public void deleteAllInBatch() {
        fortuneJpaRepository.deleteAllInBatch();
    }
    
    public Fortune findById(Long fortuneId) {
        return fortuneJpaRepository.findById(fortuneId)
            .orElseThrow(FortuneNotFoundException::new);
    }
    
    public Fortune findRandomUnassigned() {
        return fortuneJpaRepository.findRandomUnassigned()
            .orElseThrow(FortuneNotFoundException::new);
    }
    
    public void saveAll(List<Fortune> fortune) {
        fortuneJpaRepository.saveAll(fortune);
    }
    
}
