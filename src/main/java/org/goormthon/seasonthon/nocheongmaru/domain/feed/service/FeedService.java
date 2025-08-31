package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.goormthon.seasonthon.nocheongmaru.domain.comment.repository.CommentJpaRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.response.CursorPageResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.response.FeedResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.global.exception.member.FeedNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.dto.FeedIdCount;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final CommentJpaRepository commentJpaRepository;

	/**
	 * 커서 기반 페이지네이션
	 * Feed가 비어있으면 빈리스트로 반환
	 */
	@Transactional(readOnly = true)
	public CursorPageResponse<FeedResponse> getFeeds(Long cursorId, int size) {
		Pageable pageable = PageRequest.of(0, size);
		List<Feed> feeds = feedRepository.findFeedsByCursorWithMember(cursorId, pageable);

		if (feeds.isEmpty()) {
			// 빈 페이지는 정상 상황으로 처리
			return new CursorPageResponse<>(List.of(), null);
		}

		// 1) feedIds 추출
		List<Long> feedIds = feeds.stream().map(Feed::getId).toList();

		// 2) 벌크 집계 → Map 변환 (누락 id는 0L로 기본값)
		Map<Long, Long> likeMap = feedRepository.countDistinctMemberByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));
		Map<Long, Long> commentMap = commentJpaRepository.countByFeedIds(feedIds).stream()
			.collect(Collectors.toMap(FeedIdCount::getFeedId, FeedIdCount::getCount));

		// 3) DTO 매핑
		List<FeedResponse> content = feeds.stream()
			.map(f -> FeedResponse.of(
				f,
				likeMap.getOrDefault(f.getId(), 0L),
				commentMap.getOrDefault(f.getId(), 0L)
			))
			.toList();

		// 4) nextCursor 계산 (마지막 페이지면 null)
		Long lastId = feeds.get(feeds.size() - 1).getId();
		Long nextCursor = (feeds.size() < size) ? null : lastId;

		return new CursorPageResponse<>(content, nextCursor);
	}

	/**
	 * feedId로 단일 게시물 찾기
	 * DB에 ID가 없으면 FEED_NOT_FOUND에러코드 반환
	 */
	@Transactional(readOnly = true)
	public FeedResponse getFeedById(Long feedId) {
		Feed feed = feedRepository.findByIdWithMember(feedId)
			.orElseThrow(FeedNotFoundException::new);

		long likeCount = feedRepository.countDistinctMemberByFeedId(feedId);
		long commentCount = commentJpaRepository.countByFeedId(feedId);

		return FeedResponse.of(feed, likeCount, commentCount);
	}

}
