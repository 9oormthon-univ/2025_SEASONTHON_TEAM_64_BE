package org.goormthon.seasonthon.nocheongmaru.global.exception.mission;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class IsNotMissionOwnerException extends BaseException {
    
    public IsNotMissionOwnerException() {
        super(ErrorCode.IS_NOT_MISSION_OWNER);
    }
    
    public IsNotMissionOwnerException(Throwable cause) {
        super(ErrorCode.IS_NOT_MISSION_OWNER, cause);
    }
}
