package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    
}
