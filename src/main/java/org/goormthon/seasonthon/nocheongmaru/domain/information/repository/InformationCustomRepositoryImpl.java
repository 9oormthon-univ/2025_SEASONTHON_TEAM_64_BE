package org.goormthon.seasonthon.nocheongmaru.domain.information.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.image.entity.QImage.image;
import static org.goormthon.seasonthon.nocheongmaru.domain.information.entity.QInformation.information;

@RequiredArgsConstructor
@Repository
public class InformationCustomRepositoryImpl implements InformationCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    private static final long PAGE_SIZE = 8L;
    
    @Override
    public List<InformationResponse> getInformationList(Long lastId, String category, Boolean sortByRecent) {
        boolean recent = (sortByRecent == null) || sortByRecent;
        
        return queryFactory
            .select(constructor(InformationResponse.class,
                information.id,
                information.title,
                information.category.stringValue(),
                information.address,
                JPAExpressions.select(image.imageUrl)
                    .from(image)
                    .where(image.information.id.eq(information.id))
                    .limit(1)
            ))
            .from(information)
            .where(
                categoryEq(category),
                cursor(lastId, recent)
            )
            .orderBy(orderById(recent))
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    @Override
    public List<InformationResponse> getMyInformationList(Long memberId, Long lastId) {
        return queryFactory
            .select(constructor(InformationResponse.class,
                information.id,
                information.title,
                information.category.stringValue(),
                information.address,
                JPAExpressions.select(image.imageUrl)
                    .from(image)
                    .where(image.information.id.eq(information.id))
                    .limit(1)
            ))
            .from(information)
            .where(
                cursor(lastId, true),
                information.member.id.eq(memberId)
            )
            .orderBy(orderById(true))
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    private BooleanExpression categoryEq(String category) {
        if (category == null || category.isBlank()) return null;
        Category enumCategory = Category.toCategory(category);
        return information.category.eq(enumCategory);
    }
    
    private BooleanExpression cursor(Long lastId, boolean recent) {
        if (lastId == null) return null;
        return recent ? information.id.lt(lastId) : information.id.gt(lastId);
    }
    
    private OrderSpecifier<Long> orderById(boolean recent) {
        return recent ? information.id.desc() : information.id.asc();
    }
    
}
