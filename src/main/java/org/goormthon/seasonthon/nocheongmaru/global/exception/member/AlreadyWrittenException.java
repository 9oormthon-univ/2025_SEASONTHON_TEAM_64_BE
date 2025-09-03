package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class AlreadyWrittenException extends BaseException {
	public AlreadyWrittenException() {
		super(ErrorCode.ALREADY_WRITTEN);
	}

	public AlreadyWrittenException(Throwable cause) {
		super(ErrorCode.ALREADY_WRITTEN, cause);
	}
}
