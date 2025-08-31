package org.goormthon.seasonthon.nocheongmaru.domain.information.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.docs.InformationControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.InformationService;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/informations")
@RequiredArgsConstructor
@RestController
public class InformationController implements InformationControllerDocs {
    
    private final InformationService informationService;
    
    @PostMapping
    public ResponseEntity<Long> createInformation(
        @AuthMemberId Long memberId,
        @RequestBody @Valid InformationCreateRequest request
    ) {
        Long informationId = informationService.generateInformation(request.toServiceRequest(memberId));
        return ResponseEntity.ok(informationId);
    }
    
    @PutMapping("/{informationId}")
    public ResponseEntity<Long> modifyInformation(
        @AuthMemberId Long memberId,
        @PathVariable Long informationId,
        @RequestBody @Valid InformationModifyRequest request
    ) {
        Long modifyInformationId = informationService.modifyInformation(request.toServiceRequest(memberId, informationId));
        return ResponseEntity.ok(modifyInformationId);
    }
    
    @DeleteMapping("/{informationId}")
    public ResponseEntity<Void> deleteInformation(
        @AuthMemberId Long memberId,
        @PathVariable Long informationId
    ) {
        informationService.deleteInformation(memberId, informationId);
        return ResponseEntity.noContent().build();
    }
    
}
