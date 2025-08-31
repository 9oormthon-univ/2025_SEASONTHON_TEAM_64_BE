package org.goormthon.seasonthon.nocheongmaru.domain.feed.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class FeedRequest {

	private final String description;

	private final String imageUrl;
}
