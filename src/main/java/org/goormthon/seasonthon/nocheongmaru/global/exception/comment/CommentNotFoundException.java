package org.goormthon.seasonthon.nocheongmaru.global.exception.comment;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class CommentNotFoundException extends BaseException {
    
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }

    public CommentNotFoundException(Throwable cause) {
        super(ErrorCode.COMMENT_NOT_FOUND, cause);
    }
    
}
