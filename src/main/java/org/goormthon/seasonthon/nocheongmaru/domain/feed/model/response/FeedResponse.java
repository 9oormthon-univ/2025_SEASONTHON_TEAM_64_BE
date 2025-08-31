package org.goormthon.seasonthon.nocheongmaru.domain.feed.model.response;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.model.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.response.MemberResponse;

public record FeedResponse (

	Long feedId,          // 피드 PK
	String description,   // 피드 설명
	String imageUrl,      // 이미지 URL
	MemberResponse member,        // 피드 작성자 정보 (Member 엔티티)
	Long likeCount,       // 좋아요 갯수
	Long commentCount     // 댓글 갯수
) {
		// 엔티티를 FeedResponse로 변환하는 메서드
		public static FeedResponse of(Feed feed, long likeCount, long commentCount) {
			return new FeedResponse(
				feed.getId(),
				feed.getDescription(),
				feed.getImageUrl(),
				MemberResponse.of(feed.getMember()),
				likeCount,
				commentCount
			);
		}
}
