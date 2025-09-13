package org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member_fortunes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_member_assigned_at", columnNames = {"member_id", "assigned_at"})
    }
)
@Entity
public class MemberFortune {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_fortune_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fortune_id", nullable = false)
    private Fortune fortune;
    
    @Column(name = "assigned_at", nullable = false)
    private LocalDate assignedAt;
    
    @Builder
    private MemberFortune(Member member, Fortune fortune) {
        this.member = member;
        this.fortune = fortune;
        this.assignedAt = LocalDate.now();
    }
    
}
