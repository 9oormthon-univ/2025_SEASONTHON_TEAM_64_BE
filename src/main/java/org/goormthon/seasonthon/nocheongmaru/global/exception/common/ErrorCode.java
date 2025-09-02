package org.goormthon.seasonthon.nocheongmaru.global.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // S3
    IMAGE_UPLOAD_FAILED(500, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAILED(500, "이미지 삭제에 실패했습니다."),
    
    // Auth
    INVALID_ACCESS_TOKEN(400, "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(400, "유효하지 않은 리프레시 토큰입니다."),
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "접근이 금지된 사용자입니다."),
    
    // Member
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),

    // Feed
    FEED_NOT_FOUND(404, "게시물을 찾을 수 없습니다."),
    FORBIDDEN_FEED_ACCESS(403, "해당 개시물 권한 금지입니다."),

    // Mission
    MISSION_NOT_FOUND(404, "미션을 찾을 수 없습니다."),

    // Comment
    COMMENT_NOT_FOURN(404, "댓글을 찾을 수 없습니다."),
    FORBIDDEN_COMMENT_ACCESS(403, "해당 댓글 권한 금지입니다.")
    ;
    
    private final int status;
    private final String message;
    
}
