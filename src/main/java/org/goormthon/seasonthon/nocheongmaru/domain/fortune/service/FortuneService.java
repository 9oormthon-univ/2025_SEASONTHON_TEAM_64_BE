package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.request.FortuneCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FortuneService {
    
    private final MemberRepository memberRepository;
    private final FortuneGenerator fortuneGenerator;
    
    public Long generateFortune(FortuneCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        return fortuneGenerator.generateFortune(member, request.description());
    }
    
}
