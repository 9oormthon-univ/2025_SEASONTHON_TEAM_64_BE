package org.goormthon.seasonthon.nocheongmaru.global.exception.openai;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class OpenAiHttpClientException extends BaseException {
    
    public OpenAiHttpClientException() {
        super(ErrorCode.OPENAI_HTTP_CLIENT_ERROR);
    }
    
    public OpenAiHttpClientException(Throwable cause) {
        super(ErrorCode.OPENAI_HTTP_CLIENT_ERROR, cause);
    }
    
}
