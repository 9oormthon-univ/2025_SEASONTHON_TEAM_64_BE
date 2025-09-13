package org.goormthon.seasonthon.nocheongmaru.global.exception.comment;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class IsNotCommentInFeedException extends BaseException {
    
    public IsNotCommentInFeedException() {
        super(ErrorCode.IS_NOT_COMMENT_IN_FEED);
    }

    public IsNotCommentInFeedException(Throwable cause) {
        super(ErrorCode.IS_NOT_COMMENT_IN_FEED, cause);
    }
    
}
