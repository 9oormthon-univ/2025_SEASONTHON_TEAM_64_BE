package org.goormthon.seasonthon.nocheongmaru.global.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // S3
    IMAGE_UPLOAD_FAILED(500, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAILED(500, "이미지 삭제에 실패했습니다."),
    
    ;
    
    private final int status;
    private final String message;
    
}
