package org.goormthon.seasonthon.nocheongmaru.domain.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.common.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comments")
@Entity
public class Comment extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    
    @Lob
    @Column(nullable = false)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Builder
    private Comment(String description, Feed feed, Member member) {
        this.description = description;
        this.feed = feed;
        this.member = member;
    }
    
}
