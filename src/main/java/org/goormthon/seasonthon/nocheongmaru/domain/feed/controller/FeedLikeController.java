package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.docs.FeedLikeControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeMemberResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedLikeService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed-likes")
public class FeedLikeController implements FeedLikeControllerDocs {

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

	@GetMapping("/list/{feedId}")
	public ResponseEntity<List<FeedLikeMemberResponse>> getLikesByFeed(
		@PathVariable Long feedId
	) {
		List<FeedLikeMemberResponse> likes = feedLikeService.getLikesByFeed(feedId);
		return ResponseEntity.ok(likes);
	}
}
