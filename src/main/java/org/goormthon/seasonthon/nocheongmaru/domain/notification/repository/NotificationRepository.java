package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
