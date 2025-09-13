package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.exception.fortune.AlreadyGenerateFortuneException;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.FilteringProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class FortuneGenerator {
    
    private final FortuneRepository fortuneRepository;
    private final FilteringProvider filteringProvider;
    
    @Transactional
    public Long generateFortune(Member member, String description) {
        validateFortuneGeneration(member);
        filteringProvider.validateViolent(description);
        
        Fortune fortune = Fortune.builder()
            .sender(member)
            .description(description)
            .build();
        fortuneRepository.save(fortune);
        
        return fortune.getId();
    }
    
    private void validateFortuneGeneration(Member member) {
        if (fortuneRepository.existsBySenderAndForDate(member)) {
            throw new AlreadyGenerateFortuneException();
        }
    }
    
}
