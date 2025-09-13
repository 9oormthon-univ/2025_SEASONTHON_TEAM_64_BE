package org.goormthon.seasonthon.nocheongmaru.domain.member.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.global.common.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "members")
@Entity
public class Member extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(name = "profile_image_url")
    private String profileImageURL;
    
    @Column(name = "refresh_token")
    private String refreshToken;
    
    @Column(name = "device_token")
    private String deviceToken;
    
    private LocalDate lastOpenedDate;
    
    @Builder
    private Member(String email, String nickname, String profileImageURL, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.profileImageURL = profileImageURL;
    }
    
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
    
}
