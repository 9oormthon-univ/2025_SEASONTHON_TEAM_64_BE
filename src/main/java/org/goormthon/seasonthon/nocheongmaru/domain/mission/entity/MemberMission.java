package org.goormthon.seasonthon.nocheongmaru.domain.mission.entity;

import java.time.LocalDate;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_missions",
    uniqueConstraints = @UniqueConstraint(name = "uk_member_for_date", columnNames = {"member_id", "for_date"}))
@Entity
public class MemberMission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_mission_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;
    
    @Column(name = "for_date", nullable = false)
    private LocalDate forDate;
    
    @Builder
    private MemberMission(Member member, Mission mission, LocalDate forDate) {
        this.member = member;
        this.mission = mission;
        this.forDate = forDate;
    }
    
}
