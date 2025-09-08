package org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.dto.response.MissionResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.QMission.mission;

@RequiredArgsConstructor
@Repository
public class MissionListRepositoryImpl implements MissionListRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<MissionResponse> getMissionsByMember(Long memberId) {
        return queryFactory.select(constructor(MissionResponse.class,
                mission.id,
                mission.title
            ))
            .from(mission)
            .where(mission.member.id.eq(memberId))
            .fetch();
    }
    
}
