package org.goormthon.seasonthon.nocheongmaru.domain.fortune.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.entity.Fortune;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FortuneRepository {

    private final FortuneJpaRepository fortuneJpaRepository;

    public boolean existsBySenderAndCreatedAt(Member sender, LocalDate createdDate) {
        return fortuneJpaRepository.existsBySenderAndCreatedAt(sender, createdDate);
    }

    public List<Fortune> findRandomByTodayExcludingSender(Member sender, LocalDate today) {
        return fortuneJpaRepository.findRandomByTodayExcludingSender(sender, today);
    }

    public Fortune save(Fortune fortune) {
        return fortuneJpaRepository.save(fortune);
    }

    public Optional<Fortune> findById(Long id) {
        return fortuneJpaRepository.findById(id);
    }

    public List<Fortune> findByCursor(Long cursorId, int size) {
        if (cursorId == null) {
            return fortuneJpaRepository.findTopN(size);
        }
        return fortuneJpaRepository.findNextPage(cursorId, size);
    }
}
