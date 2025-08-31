package org.goormthon.seasonthon.nocheongmaru.global.exception.information;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class IsNotInformationOwnerException extends BaseException {
    
    public IsNotInformationOwnerException() {
        super(ErrorCode.IS_NOT_INFORMATION_OWNER);
    }
    
    public IsNotInformationOwnerException(Throwable cause) {
        super(ErrorCode.IS_NOT_INFORMATION_OWNER, cause);
    }
    
}
