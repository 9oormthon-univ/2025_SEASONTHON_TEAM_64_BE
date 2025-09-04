package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

import lombok.Builder;

@Builder
public record FeedLikeResponse (
	Long feedId,
	boolean liked,
	long likeCount
) {
		public static FeedLikeResponse of(Long feedId, boolean liked, long likeCount) {
			return new FeedLikeResponse(feedId, liked, likeCount);
		}
}
