package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request.CommentCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.comment.service.CommentService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/feeds/{feedId}/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping
    public ResponseEntity<Long> generateComment(
        @AuthMemberId Long memberId,
        @PathVariable Long feedId,
        @RequestBody @Valid CommentCreateRequest request
    ) {
        Long commentId = commentService.generateComment(request.toServiceRequest(memberId, feedId));
        return ResponseEntity.ok(commentId);
    }

}
