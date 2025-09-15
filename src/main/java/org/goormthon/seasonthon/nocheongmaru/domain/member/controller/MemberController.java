package org.goormthon.seasonthon.nocheongmaru.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.member.controller.dto.request.MemberDeviceTokenRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Mode;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.MemberService;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    
    private final MemberService memberService;
    
    @GetMapping
    public ResponseEntity<MemberDetailResponse> getMemberDetail(
        @AuthMemberId Long memberId
    ) {
        return ResponseEntity.ok(memberService.getMemberDetail(memberId));
    }
    
    @PutMapping("/device-token")
    public ResponseEntity<Void> updateDeviceToken(
        @AuthMemberId Long memberId,
        @RequestBody @Valid MemberDeviceTokenRequest request
    ) {
        memberService.updateDeviceToken(request.toServiceRequest(memberId));
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping
    public ResponseEntity<Void> updateMemberMode(
        @AuthMemberId Long memberId,
        @RequestParam Mode mode
    ) {
        memberService.updateMemberMode(memberId, mode);
        return ResponseEntity.noContent().build();
    }
    
}
