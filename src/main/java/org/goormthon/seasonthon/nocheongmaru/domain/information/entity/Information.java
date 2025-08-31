package org.goormthon.seasonthon.nocheongmaru.domain.information.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.common.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "informations")
@Entity
public class Information extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "information_id")
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Lob
    @Column(nullable = false)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Builder
    private Information(String title, String description, Category category, String address, Double latitude, Double longitude, Member member) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.member = member;
    }
    
}
