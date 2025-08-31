package org.goormthon.seasonthon.nocheongmaru.domain.feed.model.response;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedResponse {

	private final String description; // 피드 설명
	private final String imageUrl; // 이미지 URL
	private final Member member; // 피드 작성자 정보 (Member 엔티티)
	private final Long likeCount; // 좋아요 갯수
	private final Long commentCount; // 댓글 갯수

	// 엔티티를 FeedResponse로 변환하는 메서드
	public static FeedResponse of(Feed feed, long likeCount, long commentCount) {
		return new FeedResponse(
			feed.getDescription(),
			feed.getImageUrl(),
			feed.getMember(),
			likeCount,
			commentCount
		);
	}
}
