package org.goormthon.seasonthon.nocheongmaru.global.s3.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "S3 Image", description = "이미지 업로드/삭제 API")
public interface S3ControllerDocs {

	@Operation(
		summary = "이미지 업로드",
		description = "단일 이미지를 S3에 업로드하고 업로드된 이미지의 URL을 반환합니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "업로드 성공",
		content = @Content(schema = @Schema(implementation = String.class))
	)
	@ApiResponse(responseCode = "400", description = "파일이 비어있거나 잘못된 요청")
	ResponseEntity<String> uploadImage(
		@Parameter(description = "업로드할 이미지 파일", required = true)
		@RequestPart("file") MultipartFile file
	);

	@Operation(
		summary = "이미지 삭제",
		description = "이미지 URL을 기반으로 S3에서 이미지를 삭제합니다."
	)
	@ApiResponse(responseCode = "204", description = "삭제 성공 (내용 없음)")
	@ApiResponse(responseCode = "404", description = "해당 파일을 찾을 수 없음")
	ResponseEntity<Void> deleteImage(
		@Parameter(description = "삭제할 이미지의 S3 URL", required = true, example = "https://bucket.s3.amazonaws.com/path/to/image.png")
		@RequestParam("url") String url
	);
}
