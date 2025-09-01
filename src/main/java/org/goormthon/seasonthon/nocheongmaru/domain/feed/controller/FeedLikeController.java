package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedLikeService;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedLikes")
public class FeedLikeController {

	private final FeedLikeService feedLikeService;

	/**
	 * 게시물 좋아요 토글 API
	 */
	@PostMapping("/{feedId}/toggle")
	public ResponseEntity<FeedLikeResponse> toggle(
		@PathVariable Long feedId,
		@AuthenticationPrincipal String subject
	) {
		if (subject == null) throw new UnauthorizedException();
		Long memberId = Long.valueOf(subject);

		FeedLikeResponse r = feedLikeService.toggle(feedId, memberId);
		return ResponseEntity.ok(r);
	}
}
