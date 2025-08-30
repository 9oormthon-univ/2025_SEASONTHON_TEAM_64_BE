package org.goormthon.seasonthon.nocheongmaru.global.security.resolver;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@Component
public class AuthMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMemberId.class) &&
            parameter.getParameterType().equals(Long.class);
    }
    
    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new UnauthorizedException();
        }
        
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                throw new UnauthorizedException();
            }
        }
        
        return principal;
    }
    
}