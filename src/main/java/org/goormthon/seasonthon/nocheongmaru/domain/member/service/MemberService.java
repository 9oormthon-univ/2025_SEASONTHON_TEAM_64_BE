package org.goormthon.seasonthon.nocheongmaru.domain.member.service;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.request.MemberDeviceTokenServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId);
        
        return MemberDetailResponse.builder()
            .memberId(memberId)
            .nickname(member.getNickname())
            .profileImageUrl(member.getProfileImageURL())
            .role(member.getRole().name())
            .build();
    }
    
    @Transactional
    public void updateDeviceToken(MemberDeviceTokenServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        member.updateDeviceToken(request.deviceToken());
    }
    
}