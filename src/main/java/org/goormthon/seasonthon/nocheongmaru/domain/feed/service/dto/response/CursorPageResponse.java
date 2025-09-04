package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

import java.util.List;

public record CursorPageResponse<T>(
	List<T> content,
	Long nextCursor
) {
	public CursorPageResponse {
		content = (content == null) ? List.of() : List.copyOf(content);
	}

	public static <T> CursorPageResponse<T> of(List<T> content, Long nextCursor) {
		return new CursorPageResponse<>(content, nextCursor);
	}
}