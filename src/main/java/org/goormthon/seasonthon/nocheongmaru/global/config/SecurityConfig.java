package org.goormthon.seasonthon.nocheongmaru.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.filter.TokenAuthenticationFilter;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.handler.TokenAccessDeniedHandler;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.handler.TokenAuthenticationEntryPoint;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider.TokenProvider;
import org.goormthon.seasonthon.nocheongmaru.global.security.oauth.OAuth2KakaoService;
import org.goormthon.seasonthon.nocheongmaru.global.security.oauth.handler.Oauth2FailureHandler;
import org.goormthon.seasonthon.nocheongmaru.global.security.oauth.handler.Oauth2SuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final OAuth2KakaoService kakaoService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final Oauth2FailureHandler oauth2FailureHandler;
    private final TokenProvider tokenProvider;
    private final TokenAuthenticationEntryPoint authenticationEntryPoint;
    private final TokenAccessDeniedHandler accessDeniedHandler;
    private final ObjectMapper objectMapper;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(
                "/oauth2/**", "/login/oauth2/**", "/api/v1/auth/reissue", "/api/v1/health",
                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/h2-console/**"
            ).permitAll();
            auth.anyRequest().authenticated();
        });
        
        http.oauth2Login(oauth -> {
            oauth.loginPage("/oauth2/authorization/kakao")
                .userInfoEndpoint(userInfo -> userInfo.userService(kakaoService))
                .successHandler(oauth2SuccessHandler)
                .failureHandler(oauth2FailureHandler);
        });
        
        http
            .addFilterBefore(
                new TokenAuthenticationFilter(objectMapper, tokenProvider),
                UsernamePasswordAuthenticationFilter.class
            )
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);
            })
            .cors(cors -> cors.configurationSource(corsConfig()));
        
        return http.build();
    }
    
    @Bean
    protected CorsConfigurationSource corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
    
    @Bean
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console());
    }
    
}
