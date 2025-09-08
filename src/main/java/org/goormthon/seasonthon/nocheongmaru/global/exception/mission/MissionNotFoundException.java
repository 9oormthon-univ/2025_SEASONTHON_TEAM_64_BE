package org.goormthon.seasonthon.nocheongmaru.global.exception.mission;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class MissionNotFoundException extends BaseException {
	public MissionNotFoundException() {
		super(ErrorCode.MISSION_NOT_FOUND);
	}

	public MissionNotFoundException(Throwable cause) {
		super(ErrorCode.MISSION_NOT_FOUND, cause);
	}
}
