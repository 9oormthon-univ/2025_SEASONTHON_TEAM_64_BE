package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import java.util.Map;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.docs.FeedControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
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
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController implements FeedControllerDocs {

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

	@PostMapping("/upload")
	public ResponseEntity<FeedResponse> createFeed(
		@RequestBody @Valid FeedRequest request,
		@AuthMemberId Long memberId,
		UriComponentsBuilder uriBuilder
	) {
		if (memberId == null) throw new MemberNotFoundException();

		FeedResponse created = feedService.createFeed(memberId, request);
		return ResponseEntity
			.created(uriBuilder.path("/api/v1/feeds/{id}").buildAndExpand(created.feedId()).toUri())
			.body(created);
	}

	@DeleteMapping("/{feedId}/delete")
	public ResponseEntity<Map<String, String>> deleteFeed(
		@PathVariable Long feedId,
		@AuthMemberId Long memberId
	) {
		if (memberId == null) throw new MemberNotFoundException();

		feedService.deleteFeed(memberId, feedId);
		return ResponseEntity.ok(Map.of("message", "정상적으로 삭제되었습니다."));
	}

	@PutMapping("/{feedId}/put")
	public ResponseEntity<FeedResponse> putFeed(
		@PathVariable Long feedId,
		@RequestBody @Valid FeedPutRequest request,
		@AuthMemberId Long memberId
	) {
		if (memberId == null) throw new MemberNotFoundException();
		FeedResponse updated = feedService.replaceFeed(memberId, feedId, request);
		return ResponseEntity.ok(updated);
	}

}
