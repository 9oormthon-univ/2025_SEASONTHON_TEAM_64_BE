package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
@RestController
public class FeedController {
    
    private final FeedService feedService;
    
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
    
}
