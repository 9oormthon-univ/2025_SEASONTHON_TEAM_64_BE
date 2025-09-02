package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedPutRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request.FeedRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feedLike.FeedLikeRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.ForbiddenFeedAccessException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.FeedIdCount;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final FeedLikeRepository feedLikeRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final MissionRepository missionRepository;

	@Transactional(readOnly = true)
	public CursorPageResponse<FeedResponse> getFeeds(Long cursorId, int size) {
		Pageable pageable = PageRequest.of(0, size);
		List<Feed> feeds = feedRepository.findFeedsByCursorWithMember(cursorId, pageable);

		if (feeds.isEmpty()) {
			return new CursorPageResponse<>(List.of(), null);
		}

		List<Long> feedIds = feeds.stream().map(Feed::getId).toList();

		Map<Long, Long> likeMap = feedRepository.countDistinctMemberByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));
		Map<Long, Long> commentMap = commentRepository.countByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));

		List<FeedResponse> content = feeds.stream()
			.map(f -> FeedResponse.of(
				f,
				likeMap.getOrDefault(f.getId(), 0L),
				commentMap.getOrDefault(f.getId(), 0L)
			))
			.toList();

		Long lastId = feeds.get(feeds.size() - 1).getId();
		Long nextCursor = (feeds.size() < size) ? null : lastId;

		return new CursorPageResponse<>(content, nextCursor);
	}

	@Transactional(readOnly = true)
	public FeedResponse getFeedById(Long feedId) {
		Feed feed = feedRepository.findByIdWithMember(feedId)
			.orElseThrow(FeedNotFoundException::new);

		long likeCount = feedRepository.countDistinctMemberByFeedId(feedId);
		long commentCount = commentRepository.countByFeedId(feedId);

		return FeedResponse.of(feed, likeCount, commentCount);
	}

	@Transactional
	public FeedResponse createFeed(Long memberId, FeedRequest req) {

		if (memberId == null) throw new MemberNotFoundException();

		Member member = memberRepository.findById(memberId);
		Mission mission = missionRepository.findById(req.missionId());

		Feed saved = feedRepository.save(
			Feed.builder()
				.description(req.description())
				.imageUrl(req.imageUrl())
				.member(member)
				.mission(mission)
				.build()
		);

		return FeedResponse.of(saved, 0L, 0L);
	}

	@Transactional
	public void deleteFeed(Long memberId, Long feedId) {
		Feed feed = feedRepository.findById(feedId);

		if (!feed.getMember().getId().equals(memberId)) {
			throw new ForbiddenFeedAccessException();
		}

		commentRepository.deleteAllByFeedId(feedId);
		feedLikeRepository.deleteAllByFeedId(feedId);

		feedRepository.delete(feed);
	}

	@Transactional
	public FeedResponse replaceFeed(Long memberId, Long feedId, FeedPutRequest req) {
		// 1) 대상 피드 조회
		Feed feed = feedRepository.findById(feedId);

		// 2) 소유자 검증
		if (!feed.getMember().getId().equals(memberId)) {
			throw new ForbiddenFeedAccessException();
		}

		Mission mission = missionRepository.findById(req.missionId());

		feed.replace(req.description(), req.imageUrl(), mission);

		long likeCount = (feedLikeRepository != null) ? feedLikeRepository.countByFeed_Id(feedId) : 0L;
		long commentCount = (commentRepository != null) ? commentRepository.countByFeedId(feedId) : 0L;

		return FeedResponse.of(feed, likeCount, commentCount);
	}
}
