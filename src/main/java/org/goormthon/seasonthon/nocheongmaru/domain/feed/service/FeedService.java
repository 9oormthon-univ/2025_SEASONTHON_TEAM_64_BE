package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import java.time.LocalDate;
import java.time.ZoneId;
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
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.MemberMission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.membermission.MemberMissionRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.repository.mission.MissionRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.AlreadyUploadedTodayException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.ForbiddenFeedAccessException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.MemberNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.TodayMissionAccessDeniedException;
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
	private final MemberMissionRepository memberMissionRepo;

	@Transactional(readOnly = true)
    public CursorPageResponse<FeedResponse> getFeeds(Long cursorId, int size) {
		int pageSize = Math.min(size, 100);
		if (pageSize <= 0) {
		    throw new IllegalArgumentException("size must be positive");
		}
		Pageable pageable = PageRequest.of(0, pageSize + 1);
		List<Feed> feeds = feedRepository.findFeedsByCursorWithMember(cursorId, pageable);
		boolean hasNext = feeds.size() > pageSize;
		List<Feed> pageFeeds = hasNext ? feeds.subList(0, pageSize) : feeds;

		if (pageFeeds.isEmpty()) {
			return new CursorPageResponse<>(List.of(), null);
		}

		List<Long> feedIds = pageFeeds.stream().map(Feed::getId).toList();

		Map<Long, Long> likeMap = feedRepository.countDistinctMemberByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));
		Map<Long, Long> commentMap = commentRepository.countByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));

		List<FeedResponse> content = pageFeeds.stream()
			.map(f -> FeedResponse.of(
				f,
				likeMap.getOrDefault(f.getId(), 0L),
				commentMap.getOrDefault(f.getId(), 0L)
			))
			.toList();

		Long lastId = pageFeeds.get(pageFeeds.size() - 1).getId();
		Long nextCursor = hasNext ? lastId : null;

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

		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		MemberMission mm = memberMissionRepo.findByMemberIdAndForDate(memberId, today);

		if (req.missionId() != null && !req.missionId().equals(mm.getMission().getId())) {
			throw new TodayMissionAccessDeniedException();
		}

		Member member = memberRepository.findById(memberId);
		Mission mission = mm.getMission();

		if (feedRepository.existsByMember_IdAndMission_Id(memberId, mission.getId())) {
			throw new AlreadyUploadedTodayException();
		}

		Feed saved = feedRepository.save(
			Feed.builder()
				.description(req.description())
				.imageUrl(req.imageUrl())
				.member(member)
				.mission(mission)
				.build()
		);

		mm.markCompleted();

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
		Feed feed = feedRepository.findById(feedId);

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
