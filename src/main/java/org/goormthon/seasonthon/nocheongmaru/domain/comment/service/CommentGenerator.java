package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.CommentCreatedEvent;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.FilteringProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CommentGenerator {
    
    private final CommentRepository commentRepository;
    private final FilteringProvider filteringProvider;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public Long generateComment(Member member, Feed feed, String description) {
        filteringProvider.validateViolent(description);
        
        Comment comment = Comment.builder()
            .member(member)
            .feed(feed)
            .description(description)
            .build();
        commentRepository.save(comment);
        
        if (!member.getId().equals(feed.getMember().getId())) {
            eventPublisher.publishEvent(
                CommentCreatedEvent.builder()
                    .recipientId(feed.getMember().getId())
                    .commenterNickname(member.getNickname())
                    .feedId(feed.getId())
                    .build()
            );
        }
        
        return comment.getId();
    }
    
}
