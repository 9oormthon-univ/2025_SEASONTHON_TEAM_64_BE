package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.MemberFortune;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.fortune.FortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune.MemberFortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.exception.fortune.AlreadyAssignFortuneException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class FortuneAssigner {
    
    private final FortuneRepository fortuneRepository;
    private final MemberFortuneRepository memberFortuneRepository;
    
    @Transactional
    public FortuneResponse assignFortune(Member receiver) {
        validateFortuneAssignment(receiver);
        
        Fortune random = fortuneRepository.findRandomUnassigned();
        MemberFortune assignment = MemberFortune.builder()
            .member(receiver)
            .fortune(random)
            .build();
        memberFortuneRepository.save(assignment);
        
        return FortuneResponse.builder()
            .description(random.getDescription())
            .build();
    }
    
    private void validateFortuneAssignment(Member receiver) {
        if (memberFortuneRepository.existsAssignedToday(receiver)) {
            throw new AlreadyAssignFortuneException();
        }
    }
    
}
