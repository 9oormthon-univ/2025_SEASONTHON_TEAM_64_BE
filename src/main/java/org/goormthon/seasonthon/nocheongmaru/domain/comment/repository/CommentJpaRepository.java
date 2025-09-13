package org.goormthon.seasonthon.nocheongmaru.domain.comment.repository;


import org.goormthon.seasonthon.nocheongmaru.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends
    JpaRepository<Comment, Long>,
    CommentCustomRepository {
    
    boolean existsByIdAndFeedId(Long commentId, Long id);
    
    boolean existsByIdAndMemberId(Long commentId, Long memberId);
    
}
