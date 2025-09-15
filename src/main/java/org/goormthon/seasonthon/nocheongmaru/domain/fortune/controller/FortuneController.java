package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller;

import jakarta.validation.Valid;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.request.FortuneCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.FortuneService;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequestMapping("/api/v1/fortunes")
@RequiredArgsConstructor
@RestController
public class FortuneController {
    
    private final FortuneService fortuneService;
    
    @PostMapping
    public ResponseEntity<Long> generateFortune(
        @AuthMemberId Long memberId,
        @RequestBody @Valid FortuneCreateRequest request
    ) {
        Long fortuneId = fortuneService.generateFortune(request.toServiceRequest(memberId));
        return ResponseEntity.ok(fortuneId);
    }
    
    @PostMapping("/assign")
    public ResponseEntity<FortuneResponse> assignFortune(
        @AuthMemberId Long memberId
    ) {
        return ResponseEntity.ok(fortuneService.assignFortune(memberId));
    }
    
    @GetMapping
    public ResponseEntity<List<FortuneResponse>> getMyFortunes(
        @AuthMemberId Long memberId
    ) {
        return ResponseEntity.ok(fortuneService.getMyFortunes(memberId));
    }

}
