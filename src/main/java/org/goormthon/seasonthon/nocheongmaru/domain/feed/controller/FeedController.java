package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

	private final FeedService feedService;

	@GetMapping("/cursor")
	public ResponseEntity<CursorPageResponse<FeedResponse>> getFeeds(
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "5") int size) {

		CursorPageResponse<FeedResponse> response = feedService.getFeeds(cursorId, size);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{feedId}")
	public ResponseEntity<FeedResponse> getFeedsById(@PathVariable Long feedId) {
		FeedResponse feedResponse = feedService.getFeedById(feedId);
		return ResponseEntity.ok(feedResponse);
	}
}
