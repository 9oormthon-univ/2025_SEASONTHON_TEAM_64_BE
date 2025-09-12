package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class CommentRepository {
    
    private final CommentJpaRepository commentJpaRepository;
    
}
