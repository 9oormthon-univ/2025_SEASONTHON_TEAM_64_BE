package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
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
	private final CommentRepository commentRepository;

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

}
