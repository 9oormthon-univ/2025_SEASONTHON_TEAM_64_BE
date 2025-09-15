package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.event.FeedLikedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedLikeService {
    
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public void like(Long memberId, Long feedId) {
        Feed feed = feedRepository.findById(feedId);
        Member member = memberRepository.findById(memberId);
        
        if (feedLikeRepository.existsByFeedIdAndMemberId(feed.getId(), member.getId())) {
            return;
        }
        
        try {
            feedLikeRepository.save(
                FeedLike.builder()
                    .feed(feed)
                    .member(member)
                    .build()
            );
            
            if (!feed.getMember().getId().equals(member.getId())) {
                eventPublisher.publishEvent(
                    FeedLikedEvent.builder()
                        .recipientId(feed.getMember().getId())
                        .likerNickname(member.getNickname())
                        .feedId(feed.getId())
                        .build()
                );
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 좋아요가 존재합니다. feedId={}, memberId={}", feedId, memberId);
        }
    }
    
    @Transactional
    public void unlike(Long memberId, Long feedId) {
        feedRepository.findById(feedId);
        feedLikeRepository.deleteByFeedIdAndMemberId(feedId, memberId);
    }
    
}
