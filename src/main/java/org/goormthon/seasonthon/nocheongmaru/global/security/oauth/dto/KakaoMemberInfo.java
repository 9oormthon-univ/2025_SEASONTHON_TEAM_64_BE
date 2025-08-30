package org.goormthon.seasonthon.nocheongmaru.global.security.oauth.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record KakaoMemberInfo(
    String id,
    String email,
    String nickname,
    String profileImageUrl
) {
    public static KakaoMemberInfo of(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = getMapValue(attributes, "kakao_account");
        Map<String, Object> profile = getMapValue(kakaoAccount, "profile");
        
        return KakaoMemberInfo.builder()
            .id(String.valueOf(attributes.get("id")))
            .email(getStringValue(kakaoAccount, "email"))
            .nickname(getStringValue(profile, "nickname"))
            .profileImageUrl(getStringValue(profile, "profile_image_url"))
            .build();
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMapValue(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.get(key);
    }
    
    private static String getStringValue(Map<String, Object> map, String key) {
        return (String) map.get(key);
    }
    
}
