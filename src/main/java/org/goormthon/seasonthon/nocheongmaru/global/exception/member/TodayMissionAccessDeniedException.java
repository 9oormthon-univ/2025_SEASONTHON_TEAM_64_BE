package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class TodayMissionAccessDeniedException extends BaseException {
	public TodayMissionAccessDeniedException() {
		super(ErrorCode.TODAY_MISSION_NOT_EQUAL);
	}

	public TodayMissionAccessDeniedException(Throwable cause) {
		super(ErrorCode.TODAY_MISSION_NOT_EQUAL, cause);
	}
}
