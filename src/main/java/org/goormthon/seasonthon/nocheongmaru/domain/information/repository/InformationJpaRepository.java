package org.goormthon.seasonthon.nocheongmaru.domain.information.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationJpaRepository extends JpaRepository<Information, Long> {
}
