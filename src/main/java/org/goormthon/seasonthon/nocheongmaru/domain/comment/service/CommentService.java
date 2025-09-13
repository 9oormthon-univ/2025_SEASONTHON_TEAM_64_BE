package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.request.CommentCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    
    private final CommentGenerator commentGenerator;
    private final CommentEditor commentEditor;
    private final CommentReader commentReader;
    
    public Long generateComment(CommentCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        Feed feed = feedRepository.findById(request.feedId());
        
        return commentGenerator.generateComment(member, feed, request.description());
    }
    
    public void deleteComment(Long commentId, Long memberId, Long feedId) {
        Member member = memberRepository.findById(memberId);
        Feed feed = feedRepository.findById(feedId);
        
        commentEditor.deleteComment(commentId, member.getId(), feed.getId());
    }
    
    public List<CommentResponse> getCommentsByFeedId(Long feedId, Long memberId, Long lastCommentId) {
        Feed feed = feedRepository.findById(feedId);
        Member member = memberRepository.findById(memberId);
        
        return commentReader.getCommentsByFeedId(feed.getId(), member.getId(), lastCommentId);
    }
    
}
