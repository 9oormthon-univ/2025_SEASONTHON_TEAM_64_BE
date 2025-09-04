package org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.common.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    name = "fortunes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sender_id", "created_date"})
    }
)
@Entity
public class Fortune extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fortune_id")
    private Long id;
    
    @Lob
    @Column(nullable = false)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;
    
    @Builder
    private Fortune(String description, Member sender) {
        this.description = description;
        this.sender = sender;
    }
    
}
