package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedPutRequest (

	@NotBlank String description,
	@NotBlank String imageUrl,
	@NotNull Long missionId
) {}
