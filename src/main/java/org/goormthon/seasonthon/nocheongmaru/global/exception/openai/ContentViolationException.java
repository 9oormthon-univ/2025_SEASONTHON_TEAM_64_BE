package org.goormthon.seasonthon.nocheongmaru.global.exception.openai;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class ContentViolationException extends BaseException {
    
    public ContentViolationException() {
        super(ErrorCode.CONTENT_VIOLATION);
    }

    public ContentViolationException(Throwable cause) {
        super(ErrorCode.CONTENT_VIOLATION, cause);
    }
    
}
