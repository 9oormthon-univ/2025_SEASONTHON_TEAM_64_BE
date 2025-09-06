package org.goormthon.seasonthon.nocheongmaru.global.s3.controller;


import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class S3Controller {

	private final S3StorageUtil s3StorageUtil;

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		String url = s3StorageUtil.uploadFileToS3(file);
		return ResponseEntity.ok(url);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteImage(@RequestParam("url") String url) {
		s3StorageUtil.deleteFileFromS3(url);
		return ResponseEntity.noContent().build();
	}
}
