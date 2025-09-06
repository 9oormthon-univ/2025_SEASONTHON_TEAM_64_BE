package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.docs.CommentControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.CommentService;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response.CommentResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController implements CommentControllerDocs {

	private final CommentService commentService;

	@GetMapping("/feeds/{feedId}/comments/cursor")
	public ResponseEntity<CursorPageResponse<CommentResponse>> getComments(
		@PathVariable Long feedId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "10") int size,
		@AuthMemberId Long memberId
	) {
		CursorPageResponse<CommentResponse> response =
			commentService.getComments(feedId, cursorId, size);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/feeds/{feedId}/comments/upload")
	public ResponseEntity<CommentResponse> createComment(
		@PathVariable Long feedId,
		@RequestBody @Valid CommentRequest request,
		@AuthMemberId Long memberId,
		UriComponentsBuilder uriBuilder
	) {
		if (memberId == null) {
			throw new MemberNotFoundException();
		}

		CommentResponse created = commentService.create(feedId, memberId, request);

		return ResponseEntity
			.created(uriBuilder
				.path("/api/v1/feeds/{feedId}/comments/{commentId}")
				.buildAndExpand(feedId, created.commentId())
				.toUri())
			.body(created);
	}

	@DeleteMapping("/comments/{commentId}/delete")
	public ResponseEntity<Map<String, String>> deleteComment(
		@PathVariable Long commentId,
		@AuthMemberId Long memberId
	) {
		if (memberId == null) {
			throw new MemberNotFoundException();
		}
		commentService.delete(commentId, memberId);
		return ResponseEntity.ok(Map.of("message", "정상적으로 삭제되었습니다."));
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentResponse> putComment(
		@PathVariable Long commentId,
		@RequestBody @Valid CommentPutRequest request,
		@AuthMemberId Long memberId
	) {
		if (memberId == null) throw new MemberNotFoundException();
		CommentResponse updated = commentService.replace(commentId, memberId, request);
		return ResponseEntity.ok(updated);
	}
}
