package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MissionNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class MissionRepository {

    private final MissionJpaRepository missionJpaRepository;

    public Mission save(Mission mission) {
        return missionJpaRepository.save(mission);
    }

    public Mission findById(Long missionId) {
        return missionJpaRepository.findById(missionId)
            .orElseThrow(MissionNotFoundException::new);
    }

    public void deleteById(Long missionId) {
        missionJpaRepository.deleteById(missionId);
    }

    public List<Mission> findAllForAssignment() {
        return missionJpaRepository.findAllByOrderByIdDesc(Pageable.unpaged());
    }
    
    public void deleteAllInBatch() {
        missionJpaRepository.deleteAllInBatch();
    }
    
    public List<MissionResponse> getMissionsByMember(Long memberId) {
        return missionJpaRepository.getMissionsByMember(memberId);
    }
    
    public void saveAll(List<Mission> mission) {
        missionJpaRepository.saveAll(mission);
    }
    
}
