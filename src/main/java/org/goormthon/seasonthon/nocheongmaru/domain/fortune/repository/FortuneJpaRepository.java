package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FortuneJpaRepository extends JpaRepository<Fortune, Long> {
}
