package org.goormthon.seasonthon.nocheongmaru.domain.notification.controller.docs;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.service.dto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationControllerDocs {

	@Operation(
		summary = "알림 목록 조회",
		description = "특정 회원의 알림 목록을 조회합니다."
	)
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
	ResponseEntity<List<NotificationResponse>> list(
		@Parameter(description = "회원 ID", example = "1")
		@PathVariable Long memberId
	);

	@Operation(
		summary = "알림 읽음 처리",
		description = "알림 ID를 기반으로 읽음 상태로 변경합니다."
	)
	@ApiResponse(responseCode = "204", description = "성공적으로 읽음 처리됨")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 알림")
	ResponseEntity<Void> markAsRead(
		@Parameter(description = "알림 ID", example = "10")
		@PathVariable Long id
	);
}
