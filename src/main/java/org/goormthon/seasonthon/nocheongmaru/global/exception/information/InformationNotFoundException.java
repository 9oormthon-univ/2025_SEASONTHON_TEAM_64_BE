package org.goormthon.seasonthon.nocheongmaru.global.exception.information;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class InformationNotFoundException extends BaseException {
    
    public InformationNotFoundException() {
        super(ErrorCode.INFORMATION_NOT_FOUND);
    }
    
    public InformationNotFoundException(Throwable cause) {
        super(ErrorCode.INFORMATION_NOT_FOUND, cause);
    }
    
}
