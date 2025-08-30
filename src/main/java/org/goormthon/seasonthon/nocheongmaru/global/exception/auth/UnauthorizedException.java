package org.goormthon.seasonthon.nocheongmaru.global.exception.auth;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class UnauthorizedException extends BaseException {
    
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
    
    public UnauthorizedException(Throwable cause) {
        super(ErrorCode.UNAUTHORIZED, cause);
    }
    
}
