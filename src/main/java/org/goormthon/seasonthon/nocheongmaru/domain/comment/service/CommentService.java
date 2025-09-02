package org.goormthon.seasonthon.nocheongmaru.domain.comment.service;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.CommentNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.ForbiddenCommentAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final FeedRepository feedRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public CursorPageResponse<CommentResponse> getComments(Long feedId, Long cursorId, int size) {
		// size+1로 다음 페이지 존재 여부 확인
		Pageable pageable = PageRequest.of(0, size + 1);
		List<Comment> comments = commentRepository.findCommentsByCursorWithMember(feedId, cursorId, pageable);

		boolean hasNext = comments.size() > size;
		if (hasNext) {
			comments = comments.subList(0, size);
		}

		Long nextCursor = hasNext ? comments.get(comments.size() - 1).getId() : null;

		List<CommentResponse> content = comments.stream()
			.map(c -> CommentResponse.builder()
				.commentId(c.getId())
				.feedId(c.getFeed().getId())
				.memberId(c.getMember().getId())
				.description(c.getDescription())
				.createdAt(c.getCreatedAt())
				.build()
			)
			.toList();

		return new CursorPageResponse<>(content, nextCursor);
	}

	@Transactional
	public CommentResponse create(Long feedId, Long memberId, CommentRequest request) {
		Feed feed = feedRepository.findById(feedId);
		Member member = memberRepository.findById(memberId);

		Comment saved = commentRepository.save(
			Comment.builder()
				.description(request.description())
				.feed(feed)
				.member(member)
				.build()
		);

		return new CommentResponse(
			saved.getId(),
			feed.getId(),
			member.getId(),
			saved.getDescription(),
			saved.getCreatedAt()
		);
	}

	@Transactional
	public void delete(Long commentId, Long memberId) {
		if (!commentRepository.existsById(commentId)) {
			throw new CommentNotFoundException();
		}
		if (!commentRepository.existsByIdAndMemberId(commentId, memberId)) {
			throw new ForbiddenCommentAccessException();
		}
		commentRepository.deleteById(commentId);
	}

	@Transactional
	public CommentResponse replace(Long commentId, Long memberId, CommentPutRequest req) {
		Comment comment = commentRepository.findById(commentId);

		if (!comment.getMember().getId().equals(memberId)) {
			throw new ForbiddenCommentAccessException();
		}

		// 전체 교체
		comment.changeDescription(req.description());

		return toResponse(comment);
	}

	private CommentResponse toResponse(Comment c) {
		return new CommentResponse(
			c.getId(),
			c.getFeed().getId(),
			c.getMember().getId(),
			c.getDescription(),
			c.getCreatedAt()
		);
	}
}
