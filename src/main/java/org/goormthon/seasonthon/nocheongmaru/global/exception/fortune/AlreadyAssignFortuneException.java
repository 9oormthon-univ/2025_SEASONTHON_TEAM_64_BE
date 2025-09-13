package org.goormthon.seasonthon.nocheongmaru.global.exception.fortune;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class AlreadyAssignFortuneException extends BaseException {
    
    public AlreadyAssignFortuneException() {
        super(ErrorCode.ALREADY_ASSIGN_FORTUNE);
    }

    public AlreadyAssignFortuneException(Throwable cause) {
        super(ErrorCode.ALREADY_ASSIGN_FORTUNE, cause);
    }
    
}
