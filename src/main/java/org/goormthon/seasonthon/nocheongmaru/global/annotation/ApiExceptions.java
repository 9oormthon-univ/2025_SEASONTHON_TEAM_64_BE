package org.goormthon.seasonthon.nocheongmaru.global.annotation;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptions {
    ErrorCode[] value();
}
