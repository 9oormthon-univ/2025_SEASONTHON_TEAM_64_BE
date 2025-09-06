package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class ForbiddenCommentAccessException extends BaseException {

	public ForbiddenCommentAccessException() {
		super(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
	}

	public ForbiddenCommentAccessException(Throwable cause) {
		super(ErrorCode.FORBIDDEN_COMMENT_ACCESS, cause);
	}
}
