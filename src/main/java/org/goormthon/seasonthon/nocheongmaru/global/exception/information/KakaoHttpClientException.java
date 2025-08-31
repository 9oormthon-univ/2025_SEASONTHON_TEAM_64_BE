package org.goormthon.seasonthon.nocheongmaru.global.exception.information;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class KakaoHttpClientException extends BaseException {
    
    public KakaoHttpClientException() {
        super(ErrorCode.KAKAO_HTTP_CLIENT_ERROR);
    }
    
    public KakaoHttpClientException(Throwable cause) {
        super(ErrorCode.KAKAO_HTTP_CLIENT_ERROR, cause);
    }
}
