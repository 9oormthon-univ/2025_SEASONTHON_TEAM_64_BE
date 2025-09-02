package org.goormthon.seasonthon.nocheongmaru.domain.comment.service.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CommentResponse (
	Long commentId,
	Long feedId,
	Long memberId,
	String description,
	LocalDateTime createdAt
) {}
