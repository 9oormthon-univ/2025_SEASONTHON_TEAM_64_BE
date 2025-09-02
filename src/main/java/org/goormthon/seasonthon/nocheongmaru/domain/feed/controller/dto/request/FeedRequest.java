package org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FeedRequest(
	@NotBlank @Size(max = 2000) String description,
	@NotBlank String imageUrl,
	@NotNull Long missionId
) {}
