package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.docs;

import java.util.List;

import org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto.FortuneRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.fortune.service.dto.FortuneResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Fortune", description = "포춘쿠키 API")
public interface FortuneControllerDocs {

	@Operation(
		summary = "포춘쿠키 작성",
		description = "로그인한 사용자가 오늘의 포춘쿠키를 작성합니다. 하루에 한 번만 작성할 수 있습니다."
	)
	@ApiResponse(responseCode = "200", description = "작성 성공")
	@ApiResponse(responseCode = "400", description = "오늘 이미 작성한 경우")
	ResponseEntity<FortuneResponse> sendFortune(
		Long memberId,
		@RequestBody FortuneRequest request
	);

	@Operation(
		summary = "포춘쿠키 열기",
		description = "로그인한 사용자가 오늘의 포춘쿠키를 랜덤으로 열람합니다. 하루에 한 번만 열 수 있습니다."
	)
	@ApiResponse(responseCode = "200", description = "열람 성공")
	@ApiResponse(responseCode = "400", description = "오늘 이미 열람한 경우 / 열 수 있는 포춘쿠키가 없는 경우")
	ResponseEntity<FortuneResponse> openFortune(Long memberId);

	@Operation(
		summary = "포춘쿠키 단일 조회",
		description = "fortuneId로 특정 포춘쿠키를 조회합니다."
	)
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 포춘쿠키")
	ResponseEntity<FortuneResponse> getFortune(@PathVariable Long fortuneId);

	@Operation(
		summary = "포춘쿠키 리스트 조회 (커서 기반 페이지네이션)",
		description = "cursorId를 기준으로 최신순 포춘쿠키를 조회합니다."
	)
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<List<FortuneResponse>> getFortunes(
		@Parameter(description = "마지막으로 조회한 Fortune ID (없으면 최신부터)") @RequestParam(required = false) Long cursorId,
		@Parameter(description = "조회할 개수") @RequestParam(defaultValue = "10") int size
	);
}
