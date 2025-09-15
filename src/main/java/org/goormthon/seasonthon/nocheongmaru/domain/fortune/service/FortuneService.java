package org.goormthon.seasonthon.nocheongmaru.domain.fortune.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.request.FortuneCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FortuneService {
    
    private final MemberRepository memberRepository;
    private final FortuneGenerator fortuneGenerator;
    private final FortuneAssigner fortuneAssigner;
    private final FortuneReader fortuneReader;
    
    public Long generateFortune(FortuneCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        return fortuneGenerator.generateFortune(member, request.description());
    }
    
    public FortuneResponse assignFortune(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return fortuneAssigner.assignFortune(member);
    }
    
    public List<FortuneResponse> getMyFortunes(Long memberId) {
        return fortuneReader.getMyFortunes(memberId);
    }
    
}
