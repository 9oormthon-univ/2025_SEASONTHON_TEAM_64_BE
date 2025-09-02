package org.goormthon.seasonthon.nocheongmaru.global.exception.information;

import lombok.Getter;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

@Getter
public class AddressNotFoundException extends BaseException {
    
    public AddressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }
    
    public AddressNotFoundException(Throwable cause) {
        super(ErrorCode.ADDRESS_NOT_FOUND, cause);
    }
    
}
