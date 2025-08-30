package org.goormthon.seasonthon.nocheongmaru.global.config;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.global.security.resolver.AuthMemberIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final AuthMemberIdArgumentResolver authMemberIdArgumentResolver;
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberIdArgumentResolver);
    }
    
}
