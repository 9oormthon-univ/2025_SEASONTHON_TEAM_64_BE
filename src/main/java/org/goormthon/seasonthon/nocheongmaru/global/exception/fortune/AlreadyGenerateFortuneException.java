package org.goormthon.seasonthon.nocheongmaru.global.exception.fortune;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class AlreadyGenerateFortuneException extends BaseException {
    
    public AlreadyGenerateFortuneException() {
        super(ErrorCode.ALREADY_GENERATE_FORTUNE);
    }
    
    public AlreadyGenerateFortuneException(Throwable cause) {
        super(ErrorCode.ALREADY_GENERATE_FORTUNE, cause);
    }
    
}
