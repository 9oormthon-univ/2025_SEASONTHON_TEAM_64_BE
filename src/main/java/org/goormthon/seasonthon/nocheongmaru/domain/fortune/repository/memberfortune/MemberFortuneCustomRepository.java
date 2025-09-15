package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;

import java.util.List;

public interface MemberFortuneCustomRepository {
    
    List<FortuneResponse> getMyFortunes(Long memberId);
    
}
