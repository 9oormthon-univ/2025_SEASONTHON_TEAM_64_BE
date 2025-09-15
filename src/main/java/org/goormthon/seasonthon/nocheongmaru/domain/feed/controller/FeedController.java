package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedLikeService;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
@RestController
public class FeedController {
    
    private final FeedService feedService;
    private final FeedLikeService feedLikeService;
    
    @PostMapping
    public ResponseEntity<Long> generateFeed(
        @AuthMemberId Long memberId,
        @RequestPart @Valid FeedCreateRequest request,
        @RequestPart MultipartFile imageFile
    ) {
        Long feedId = feedService.generateFeed(request.toServiceRequest(memberId, imageFile));
        return ResponseEntity.ok(feedId);
    }
    
    @PutMapping("/{feedId}")
    public ResponseEntity<Void> modifyFeed(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId,
        @RequestPart @Valid FeedModifyRequest request,
        @RequestPart MultipartFile imageFile
    ) {
        feedService.modifyFeed(request.toServiceRequest(memberId, feedId, imageFile));
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId
    ) {
        feedService.deleteFeed(memberId, feedId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponse> getFeed(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId
    ) {
        FeedResponse response = feedService.getFeed(memberId, feedId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<List<FeedResponse>> getMyFeeds(
        @AuthMemberId Long memberId,
        @RequestParam(required = false) Long lastFeedId
    ) {
        List<FeedResponse> responses = feedService.getMyFeeds(memberId, lastFeedId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping
    public ResponseEntity<List<FeedResponse>> getFeeds(
        @AuthMemberId Long memberId,
        @RequestParam(required = false) Long lastFeedId
    ) {
        List<FeedResponse> responses = feedService.getFeeds(memberId, lastFeedId);
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/{feedId}/like")
    public ResponseEntity<Void> like(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId
    ) {
        feedLikeService.like(memberId, feedId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{feedId}/like")
    public ResponseEntity<Void> unlike(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId
    ) {
        feedLikeService.unlike(memberId, feedId);
        return ResponseEntity.ok().build();
    }
    
}
