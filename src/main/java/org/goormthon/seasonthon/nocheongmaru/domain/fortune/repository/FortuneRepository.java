package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FortuneRepository {
    
    private final FortuneJpaRepository fortuneJpaRepository;
    
}
