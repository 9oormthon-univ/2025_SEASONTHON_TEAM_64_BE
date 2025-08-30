package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class MemberNotFoundException extends BaseException {
    
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
    
    public MemberNotFoundException(Throwable cause) {
        super(ErrorCode.MEMBER_NOT_FOUND, cause);
    }
}
