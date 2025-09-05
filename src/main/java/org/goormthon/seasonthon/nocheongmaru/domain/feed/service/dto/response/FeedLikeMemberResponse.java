package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

public record FeedLikeMemberResponse (
	Long memberId,
	String nickname,
	String profileImageUrl
) {}