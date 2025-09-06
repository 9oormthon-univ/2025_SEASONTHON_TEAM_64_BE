package org.goormthon.seasonthon.nocheongmaru.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentPutRequest (
	@NotBlank String description
){}
