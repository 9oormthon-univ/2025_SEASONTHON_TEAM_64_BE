package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class AlreadyOpenException extends BaseException {
	public AlreadyOpenException() {
		super(ErrorCode.ALREADY_OPEN);
	}

	public AlreadyOpenException(Throwable cause) {
		super(ErrorCode.ALREADY_OPEN, cause);
	}
}
