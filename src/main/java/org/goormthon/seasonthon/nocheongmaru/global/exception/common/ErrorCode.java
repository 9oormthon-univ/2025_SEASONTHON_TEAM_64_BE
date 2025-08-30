package org.goormthon.seasonthon.nocheongmaru.global.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    ;
    
    private final int status;
    private final String message;
    
}
