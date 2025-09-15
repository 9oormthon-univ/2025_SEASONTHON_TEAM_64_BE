package org.goormthon.seasonthon.nocheongmaru.domain.information.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.docs.InformationControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.dto.request.InformationModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.InformationService;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationDetailResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/informations")
@RequiredArgsConstructor
@RestController
public class InformationController implements InformationControllerDocs {
    
    private final InformationService informationService;
    
    @GetMapping
    public ResponseEntity<List<InformationResponse>> getInformationList(
        @RequestParam(required = false) Long lastId,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Boolean sortByRecent
    ) {
        return ResponseEntity.ok(informationService.getInformationList(lastId, category, sortByRecent));
    }
    
    @GetMapping("/me")
    public ResponseEntity<List<InformationResponse>> getMyInformationList(
        @AuthMemberId Long memberId,
        @RequestParam(required = false) Long lastId
    ) {
        return ResponseEntity.ok(informationService.getMyInformationList(memberId, lastId));
    }
    
    @PostMapping
    public ResponseEntity<Long> createInformation(
        @AuthMemberId Long memberId,
        @RequestPart @Valid InformationCreateRequest request,
        @RequestPart(required = false) List<MultipartFile> images
    ) {
        Long informationId = informationService.generateInformation(request.toServiceRequest(memberId, images));
        return ResponseEntity.ok(informationId);
    }
    
    @GetMapping("/{informationId}")
    public ResponseEntity<InformationDetailResponse> getInformationDetail(
        @PathVariable Long informationId
    ) {
        InformationDetailResponse response = informationService.getInformationDetail(informationId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{informationId}")
    public ResponseEntity<Long> modifyInformation(
        @AuthMemberId Long memberId,
        @PathVariable Long informationId,
        @RequestPart @Valid InformationModifyRequest request,
        @RequestPart(required = false) List<MultipartFile> images
    ) {
        Long modifyInformationId = informationService.modifyInformation(request.toServiceRequest(memberId, informationId, images));
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
