package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationJpaRepository extends
    JpaRepository<Notification, Long>,
    NotificationCustomRepository {
    
    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM Notification n WHERE n.member.id = :memberId AND n.isRead = false")
    boolean existsByMemberIdAndIsRead(@Param("memberId") Long memberId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.member.id = :memberId AND n.isRead = false")
    void markAllAsIsRead(@Param("memberId") Long memberId);
    
}
