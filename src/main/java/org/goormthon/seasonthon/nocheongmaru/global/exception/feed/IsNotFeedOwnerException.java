package org.goormthon.seasonthon.nocheongmaru.global.exception.feed;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class IsNotFeedOwnerException extends BaseException {
    
    public IsNotFeedOwnerException() {
        super(ErrorCode.IS_NOT_FEED_OWNER);
    }
    
    public IsNotFeedOwnerException(Throwable cause) {
        super(ErrorCode.IS_NOT_FEED_OWNER, cause);
    }
    
}
