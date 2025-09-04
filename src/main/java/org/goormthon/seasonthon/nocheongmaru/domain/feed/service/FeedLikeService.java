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

		long deleted = feedLikeRepository.deleteByFeed_IdAndMember_Id(feedId, memberId);
		boolean liked;

		if (deleted > 0) {
			liked = false;
		} else {
			try {
				var memberRef = em.getReference(Member.class, memberId);
				feedLikeRepository.save(
					FeedLike.builder()
						.feed(feed)
						.member(memberRef)
						.build()
				);
				liked = true;
				}
			catch (DataIntegrityViolationException dup) {
			    if (feedLikeRepository.existsByFeed_IdAndMember_Id(feedId, memberId)) {
					liked = true;
				} else {
					throw dup;
				}
			}
		}

		em.flush();
		long likeCount = feedLikeRepository.countByFeed_Id(feedId);
		return FeedLikeResponse.of(feedId, liked, likeCount);
	}


}
