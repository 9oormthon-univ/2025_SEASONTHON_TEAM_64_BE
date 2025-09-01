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

}
