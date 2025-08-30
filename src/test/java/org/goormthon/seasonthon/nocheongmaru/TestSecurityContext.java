package org.goormthon.seasonthon.nocheongmaru;

import org.goormthon.seasonthon.nocheongmaru.global.annotation.TestMember;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSecurityContext implements WithSecurityContextFactory<TestMember> {
    
    @Override
    public SecurityContext createSecurityContext(TestMember annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        
        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.roles())
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
        
        Long memberId = annotation.memberId();
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(memberId, null, authorities);
        securityContext.setAuthentication(authentication);
        
        return securityContext;
    }
    
}
