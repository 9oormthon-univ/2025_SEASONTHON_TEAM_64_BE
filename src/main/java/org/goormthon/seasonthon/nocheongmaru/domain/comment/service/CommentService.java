package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.request.CommentCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    
    private final CommentGenerator commentGenerator;
    
    public Long generateComment(CommentCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        Feed feed = feedRepository.findById(request.feedId());
        
        return commentGenerator.generateComment(member, feed, request.description());
    }
    
}
