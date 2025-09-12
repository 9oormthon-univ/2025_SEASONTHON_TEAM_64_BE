package org.goormthon.seasonthon.nocheongmaru.domain.notification.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
