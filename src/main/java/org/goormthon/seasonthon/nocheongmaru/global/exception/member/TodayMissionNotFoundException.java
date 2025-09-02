package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class TodayMissionNotFoundException extends BaseException {
	public TodayMissionNotFoundException() {
		super(ErrorCode.TODAY_MISSION_NOT_FOUND);
	}

	public TodayMissionNotFoundException(Throwable cause) {
		super(ErrorCode.TODAY_MISSION_NOT_FOUND, cause);
	}
}
