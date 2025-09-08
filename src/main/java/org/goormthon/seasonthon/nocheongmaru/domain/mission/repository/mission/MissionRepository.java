package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
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

    public List<Mission> findAll() {
        return missionJpaRepository.findAll();
    }

    public Mission findById(Long missionId) {
        return missionJpaRepository.findById(missionId)
            .orElseThrow(MissionNotFoundException::new);
    }

    public Mission getReferenceById(Long missionId) {
        return missionJpaRepository.getReferenceById(missionId);
    }

    public boolean existsById(Long missionId) {
        return missionJpaRepository.existsById(missionId);
    }

    public void deleteById(Long missionId) {
        missionJpaRepository.deleteById(missionId);
    }

    public List<Mission> findAllForAssignment() {
        return missionJpaRepository.findAllByOrderByIdDesc(Pageable.unpaged());
    }

    public List<Mission> findAllForAssignment(Long cursorId, Pageable pageable) {
        if (cursorId == null) {
            return missionJpaRepository.findAllByOrderByIdDesc(pageable);
        }
        return missionJpaRepository.findByIdLessThanOrderByIdDesc(cursorId, pageable);
    }

    public long deleteAll() {
        return missionJpaRepository.deleteAllInBulk();
    }
}
