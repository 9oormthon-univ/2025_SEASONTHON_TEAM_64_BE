package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository.memberfortune;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.response.FortuneResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.QMemberFortune.memberFortune;

@RequiredArgsConstructor
@Repository
public class MemberFortuneCustomRepositoryImpl implements MemberFortuneCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<FortuneResponse> getMyFortunes(Long memberId) {
        return queryFactory.select(constructor(FortuneResponse.class,
                memberFortune.fortune.description
            ))
            .from(memberFortune)
            .where(memberFortune.member.id.eq(memberId))
            .fetch();
    }
    
}
