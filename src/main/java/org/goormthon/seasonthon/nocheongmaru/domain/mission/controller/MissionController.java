package org.goormthon.seasonthon.nocheongmaru.domain.mission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request.MissionCreateRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.dto.request.MissionModifyRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.MissionService;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.AuthMemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
@RestController
public class MissionController {
    
    private final MissionService missionService;
    
    @PostMapping("/admin")
    public ResponseEntity<Long> generateMission(
        @AuthMemberId Long memberId,
        @RequestBody @Valid MissionCreateRequest request
    ) {
        Long missionId = missionService.generate(request.toServiceRequest(memberId));
        return ResponseEntity.ok(missionId);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<List<MissionResponse>> getMissionsByMember(
        @AuthMemberId Long memberId
    ) {
        List<MissionResponse> missions = missionService.getMissionsByMember(memberId);
        return ResponseEntity.ok(missions);
    }
    
    @GetMapping("/admin/{missionId}")
    public ResponseEntity<MissionResponse> getMissionByMember(
        @PathVariable Long missionId
    ) {
        MissionResponse mission = missionService.getMissionByMember(missionId);
        return ResponseEntity.ok(mission);
    }
    
    @DeleteMapping("/admin/{missionId}")
    public ResponseEntity<Void> deleteMission(
        @AuthMemberId Long memberId,
        @PathVariable Long missionId
    ) {
        missionService.delete(memberId, missionId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/admin")
    public ResponseEntity<Void> modifyMission(
        @AuthMemberId Long memberId,
        @RequestBody @Valid MissionModifyRequest request
    ) {
        missionService.modify(request.toServiceRequest(memberId));
        return ResponseEntity.noContent().build();
    }
    
}
