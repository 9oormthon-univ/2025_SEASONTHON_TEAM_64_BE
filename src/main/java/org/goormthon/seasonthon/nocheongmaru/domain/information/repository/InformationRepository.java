package org.goormthon.seasonthon.nocheongmaru.domain.information.repository;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.InformationNotFoundException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InformationRepository {
    
    private final InformationJpaRepository informationJpaRepository;
    
    public void save(Information information) {
        informationJpaRepository.save(information);
    }
    
    public void deleteAllInBatch() {
        informationJpaRepository.deleteAllInBatch();
    }
    
    public Information findById(Long informationId) {
        return informationJpaRepository.findById(informationId)
            .orElseThrow(InformationNotFoundException::new);
    }
    
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return informationJpaRepository.existsByIdAndMemberId(id, memberId);
    }
    
    public void deleteById(Long informationId) {
        informationJpaRepository.deleteById(informationId);
    }
    
    public boolean existsById(Long id) {
        return informationJpaRepository.existsById(id);
    }
    
}
