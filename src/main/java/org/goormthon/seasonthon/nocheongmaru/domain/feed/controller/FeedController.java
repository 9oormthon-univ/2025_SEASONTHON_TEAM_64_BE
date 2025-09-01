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

	/**
	 * 커서 기반 페이지네이션을 위한 피드 조회 API
	 * @param cursorId  : 현재 페이지의 마지막 피드 ID (null이면 첫 페이지)
	 * @param size      : 페이지 크기 (몇 개의 피드를 반환할지)
	 * @return          : FeedResponse 리스트와 다음 커서 ID
	 */
	@GetMapping("/cursor")
	public ResponseEntity<CursorPageResponse<FeedResponse>> getFeeds(
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "5") int size) {

		CursorPageResponse<FeedResponse> response = feedService.getFeeds(cursorId, size);

		return ResponseEntity.ok(response);
	}

	/**
	 * 게시물 ID로 조회
	 * @param feedId : 피드 ID로 조회
	 * @return : FeedResponse
	 */
	@GetMapping("/{feedId}")
	public ResponseEntity<FeedResponse> getFeedsById(@PathVariable Long feedId) {
		FeedResponse feedResponse = feedService.getFeedById(feedId);
		return ResponseEntity.ok(feedResponse);
	}
}
