package org.goormthon.seasonthon.nocheongmaru.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}
