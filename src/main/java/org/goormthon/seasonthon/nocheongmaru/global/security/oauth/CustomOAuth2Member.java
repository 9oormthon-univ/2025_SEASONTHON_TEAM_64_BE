package org.goormthon.seasonthon.nocheongmaru.global.security.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2Member extends DefaultOAuth2User {
    
    private final Long memberId;
    
    public CustomOAuth2Member(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes,
        String nameAttributeKey,
        Long memberId
    ) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
    }
    
}
