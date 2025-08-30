package org.goormthon.seasonthon.nocheongmaru.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.security.oauth.dto.KakaoMemberInfo;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class OAuth2KakaoService extends DefaultOAuth2UserService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoMemberInfo kakaoMemberInfo = KakaoMemberInfo.of(oAuth2User.getAttributes());
        
        Member member = getOrCreateMember(kakaoMemberInfo);
        
        return new CustomOAuth2Member(
            Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
            oAuth2User.getAttributes(),
            "id",
            member.getId()
        );
    }
    
    private Member getOrCreateMember(KakaoMemberInfo kakaoMemberInfo) {
        try {
            return memberRepository.findByEmail(kakaoMemberInfo.email());
        } catch (MemberNotFoundException e) {
            Member member = createMember(kakaoMemberInfo);
            memberRepository.save(member);
            return member;
        }
    }
    
    private Member createMember(KakaoMemberInfo kakaoMemberInfo) {
        return Member.builder()
            .email(kakaoMemberInfo.email())
            .nickname(kakaoMemberInfo.nickname())
            .profileImageURL(kakaoMemberInfo.profileImageUrl())
            .role(Role.ROLE_USER)
            .build();
    }
    
}
