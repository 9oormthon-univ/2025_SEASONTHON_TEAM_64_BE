package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune.MemberFortuneRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FortuneReader {
    
    private final MemberFortuneRepository memberFortuneRepository;
    
    @Transactional(readOnly = true)
    public List<FortuneResponse> getMyFortunes(Long memberId) {
        return memberFortuneRepository.getMyFortunes(memberId);
    }
}
