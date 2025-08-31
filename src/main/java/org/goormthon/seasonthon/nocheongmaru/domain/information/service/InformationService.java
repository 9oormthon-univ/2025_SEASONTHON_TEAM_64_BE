package org.goormthon.seasonthon.nocheongmaru.domain.information.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.request.InformationCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InformationService {
    
    private final MemberRepository memberRepository;
    private final InformationGenerator informationGenerator;
    
    public Long generateInformation(InformationCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        
        return informationGenerator.generate(
            member,
            request.title(),
            request.description(),
            request.address(),
            request.category()
        );
    }
    
}
