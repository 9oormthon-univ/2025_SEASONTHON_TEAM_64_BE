package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.FeedLike;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedLikeResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.auth.UnauthorizedException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

	private final FeedRepository feedRepository;
	private final FeedLikeRepository feedLikeRepository;
	private final MemberRepository memberRepository;

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public FeedLikeResponse toggle(Long feedId, Long memberId) {
		if (memberId == null) throw new UnauthorizedException();

		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(FeedNotFoundException::new);

		if (!memberRepository.existsById(memberId)) {
			throw new MemberNotFoundException();
		}

		// 1) 먼저 '취소' 시도 (이미 눌러져 있었다면 여기서 1건 삭제됨)
		long deleted = feedLikeRepository.deleteByFeed_IdAndMember_Id(feedId, memberId);
		boolean liked;

		if (deleted > 0) {
			// 이미 좋아요였고, 이번 호출로 취소됨
			liked = false;
		} else {
			// 2) 이전에 좋아요가 아니었으므로 '등록' 시도
			try {
				var memberRef = em.getReference(Member.class, memberId);
				feedLikeRepository.save(
					FeedLike.builder()
						.feed(feed)
						.member(memberRef)
						.build()
				);
				liked = true;
			} catch (DataIntegrityViolationException dup) {
				// 동시 삽입 경합으로 인한 유니크 충돌 → 이미 좋아요라고 간주
				liked = true;
			}
		}

		long likeCount = feedLikeRepository.countByFeed_Id(feedId);
		return FeedLikeResponse.of(feedId, liked, likeCount);
	}


}
