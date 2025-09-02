package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MissionNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class MissionRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;
    private final MissionJpaRepository missionJpaRepository;

    public Mission save(Mission mission) {
        return missionJpaRepository.save(mission);
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
        return query.selectFrom(mission)
            .orderBy(mission.id.desc())
            .fetch();
    }

    public List<Mission> findAllForAssignment(Long cursorId, Pageable pageable) {
        return query.selectFrom(mission)
            .where(cursorId == null ? null : mission.id.lt(cursorId))
            .orderBy(mission.id.desc())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public long deleteAll() {
        long deleted = query.delete(mission).execute();
        em.clear();
        return deleted;
    }
}
