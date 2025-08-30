package org.goormthon.seasonthon.nocheongmaru.global.exception.s3;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class ImageDeleteException extends BaseException {
    
    public ImageDeleteException() {
        super(ErrorCode.IMAGE_DELETE_FAILED);
    }
    
    public ImageDeleteException(Throwable cause) {
        super(ErrorCode.IMAGE_DELETE_FAILED, cause);
    }
    
}
