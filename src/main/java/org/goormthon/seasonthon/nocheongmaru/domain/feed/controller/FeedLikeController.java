package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedLikeService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed-likes")
public class FeedLikeController {

	private final FeedLikeService feedLikeService;

	@PostMapping("/{feedId}/toggle")
	public ResponseEntity<FeedLikeResponse> toggle(
		@PathVariable Long feedId,
		@AuthMemberId Long memberId
	) {
		if (memberId == null) {
			throw new UnauthorizedException();
		}

		FeedLikeResponse r = feedLikeService.toggle(feedId, memberId);
		return ResponseEntity.ok(r);
	}
}
