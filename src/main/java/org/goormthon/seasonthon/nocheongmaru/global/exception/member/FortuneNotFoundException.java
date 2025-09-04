package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class FortuneNotFoundException extends BaseException {
	public FortuneNotFoundException() {
		super(ErrorCode.FORTUNE_NOT_FOUND);
	}

	public FortuneNotFoundException(Throwable cause) {
		super(ErrorCode.FORTUNE_NOT_FOUND, cause);
	}
}
