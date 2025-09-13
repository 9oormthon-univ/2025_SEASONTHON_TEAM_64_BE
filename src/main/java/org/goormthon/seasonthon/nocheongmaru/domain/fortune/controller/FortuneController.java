package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller;

import jakarta.validation.Valid;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.request.FortuneCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.FortuneService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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

}

