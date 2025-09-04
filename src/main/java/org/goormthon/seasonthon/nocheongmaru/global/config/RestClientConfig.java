package org.goormthon.seasonthon.nocheongmaru.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientConfig {
    
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String restApiKey;
    
    private static final String KAKAO_MAP_BASE_URL = "https://dapi.kakao.com/v2";
    private static final String KAKAO_MAP_HEADER_PREFIX = "KakaoAK ";
    private static final String HEADER_TYPE = "Authorization";
    
    public RestClient kakaoRestClient() {
        return RestClient.builder()
            .baseUrl(KAKAO_MAP_BASE_URL)
            .defaultHeader(HEADER_TYPE, KAKAO_MAP_HEADER_PREFIX + restApiKey)
            .build();
    }
    
}
