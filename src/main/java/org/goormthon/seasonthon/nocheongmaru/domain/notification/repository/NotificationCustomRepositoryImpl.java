package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.response.NotificationResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.QNotification.notification;

@RequiredArgsConstructor
@Repository
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {
    
    private final JPAQueryFactory queryFactory;
    private static final long PAGE_SIZE = 10L;
    
    @Override
    public List<NotificationResponse> getNotifications(Long memberId, Long lastNotificationId) {
        return queryFactory.select(constructor(NotificationResponse.class,
                notification.type,
                notification.message,
                notification.isRead,
                notification.createdAt
            ))
            .from(notification)
            .where(
                notification.member.id.eq(memberId),
                cursor(lastNotificationId)
            )
            .orderBy(notification.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }
    
    private BooleanExpression cursor(Long lastId) {
        return lastId == null ? null : notification.id.lt(lastId);
    }
    
}
