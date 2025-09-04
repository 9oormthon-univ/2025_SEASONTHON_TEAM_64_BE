package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FeedRequest(
	@NotBlank String description,
	@NotBlank String imageUrl
) {}
