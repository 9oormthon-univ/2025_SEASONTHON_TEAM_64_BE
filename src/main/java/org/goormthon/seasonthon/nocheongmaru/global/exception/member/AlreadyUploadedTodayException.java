package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class AlreadyUploadedTodayException extends BaseException {
	public AlreadyUploadedTodayException() {
		super(ErrorCode.ALREADY_UPLOADED_TODAY);
	}

	public AlreadyUploadedTodayException(Throwable cause) {
		super(ErrorCode.ALREADY_UPLOADED_TODAY, cause);
	}
}
