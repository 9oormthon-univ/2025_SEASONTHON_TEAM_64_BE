package org.goormthon.seasonthon.nocheongmaru.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Mode;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.request.MemberDeviceTokenServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .email(member.getEmail())
            .profileImageUrl(member.getProfileImageURL())
            .role(member.getRole().name())
            .mode(member.getMode() != null ? member.getMode().name() : null)
            .build();
    }
    
    @Transactional
    public void updateDeviceToken(MemberDeviceTokenServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        member.updateDeviceToken(request.deviceToken());
    }
    
    @Transactional
    public void updateMemberMode(Long memberId, Mode mode) {
        Member member = memberRepository.findById(memberId);
        member.updateMode(mode);
    }
    
}