package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto;

public class FeedIdCount {

	private final Long feedId;
	private final Long count;

	public FeedIdCount(Long feedId, Long count) {
		this.feedId = feedId;
		this.count = count;
	}
	public Long getFeedId() { return feedId; }
	public Long getCount() { return count; }
}
