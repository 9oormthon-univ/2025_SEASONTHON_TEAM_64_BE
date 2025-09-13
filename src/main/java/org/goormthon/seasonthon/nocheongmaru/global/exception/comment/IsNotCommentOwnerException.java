package org.goormthon.seasonthon.nocheongmaru.global.exception.comment;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class IsNotCommentOwnerException extends BaseException {
    
    public IsNotCommentOwnerException() {
        super(ErrorCode.IS_NOT_COMMENT_OWNER);
    }

    public IsNotCommentOwnerException(Throwable cause) {
        super(ErrorCode.IS_NOT_COMMENT_OWNER, cause);
    }
    
}
