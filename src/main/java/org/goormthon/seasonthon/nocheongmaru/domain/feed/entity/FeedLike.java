package org.goormthon.seasonthon.nocheongmaru.domain.feed.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "feed_like",
    uniqueConstraints = @UniqueConstraint(name = "uk_feed_member",
        columnNames = {"feed_id","member_id"}))
@Entity
public class FeedLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Builder
    private FeedLike(Feed feed, Member member) {
        this.feed = feed;
        this.member = member;
    }
    
}
