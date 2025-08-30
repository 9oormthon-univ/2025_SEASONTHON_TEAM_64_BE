package org.goormthon.seasonthon.nocheongmaru.global.exception.auth;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class InvalidRefreshTokenException extends BaseException {
    
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    
    public InvalidRefreshTokenException(Throwable cause) {
        super(ErrorCode.INVALID_REFRESH_TOKEN, cause);
    }
    
}
