package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response.MemberResponse;

import lombok.Builder;

@Builder
public record FeedResponse (

	Long feedId,
	String description,
	String imageUrl,
	MemberResponse member,
	Long likeCount,
	Long commentCount,
	Long missionId
) {
		public static FeedResponse of(Feed feed, long likeCount, long commentCount) {
			return new FeedResponse(
				feed.getId(),
				feed.getDescription(),
				feed.getImageUrl(),
				MemberResponse.of(feed.getMember()),
				likeCount,
				commentCount,
				feed.getMission().getId()
			);
		}
}
