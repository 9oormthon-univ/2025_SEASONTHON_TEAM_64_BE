package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class ForbiddenFeedAccessException extends BaseException {
	public ForbiddenFeedAccessException() {
		super(ErrorCode.FORBIDDEN_FEED_ACCESS);
	}

	public ForbiddenFeedAccessException(Throwable cause) {
		super(ErrorCode.FORBIDDEN_FEED_ACCESS, cause);
	}
}
