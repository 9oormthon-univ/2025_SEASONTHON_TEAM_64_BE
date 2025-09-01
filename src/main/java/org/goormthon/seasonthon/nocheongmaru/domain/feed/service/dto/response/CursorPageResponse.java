package org.goormthon.seasonthon.nocheongmaru.domain.feed.service.dto.response;

import java.util.List;

public record CursorPageResponse<T>(
	List<T> content,
	Long nextCursor // 다음 페이지 시작점 (null이면 더 없음)
) {
	// compact constructor: 불변/널 안전
	public CursorPageResponse {
		content = (content == null) ? List.of() : List.copyOf(content);
	}

	// 편의 팩토리 (선택)
	public static <T> CursorPageResponse<T> of(List<T> content, Long nextCursor) {
		return new CursorPageResponse<>(content, nextCursor);
	}
}