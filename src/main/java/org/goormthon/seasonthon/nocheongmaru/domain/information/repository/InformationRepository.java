package org.goormthon.seasonthon.nocheongmaru.domain.information.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InformationRepository {
    
    private final InformationJpaRepository informationJpaRepository;
    
}
