package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);

	@Query("select n from Notification n join fetch n.member m where m.id = :memberId order by n.createdAt desc")
	List<Notification> findByMemberIdWithMember(@Param("memberId") Long memberId);
}
