package org.goormthon.seasonthon.nocheongmaru.global.exception.s3;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class ImageUploadException extends BaseException {
    
    public ImageUploadException() {
        super(ErrorCode.IMAGE_UPLOAD_FAILED);
    }
    
    public ImageUploadException(Throwable cause) {
        super(ErrorCode.IMAGE_UPLOAD_FAILED, cause);
    }
    
}
