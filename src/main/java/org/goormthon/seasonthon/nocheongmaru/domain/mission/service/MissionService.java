package org.goormthon.seasonthon.nocheongmaru.domain.mission.service;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionCreateServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.request.MissionModifyServiceRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MissionService {

    private final MemberRepository memberRepository;
    private final MissionReader missionReader;
    private final MissionEditor missionEditor;
    private final MissionGenerator missionGenerator;
    
    public Long generate(MissionCreateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        return missionGenerator.generate(request.missionDescription(), member);
    }
    
    public void modify(MissionModifyServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());
        missionEditor.modifyMission(member.getId(), request.missionId(), request.missionDescription());
    }
    
    public void delete(Long memberId, Long missionId) {
        Member member = memberRepository.findById(memberId);
        missionEditor.deleteMission(member.getId(), missionId);
    }
    
    public List<MissionResponse> getMissionsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return missionReader.getMissionsByMember(member.getId());
    }
    
    public MissionResponse getMissionByMember(Long missionId) {
        return missionReader.getMissionByMember(missionId);
    }
    
}
