package org.goormthon.seasonthon.nocheongmaru.global.annotation;


import org.goormthon.seasonthon.nocheongmaru.TestSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContext.class)
public @interface TestMember {
    
    long memberId() default 1L;
    
    String[] roles() default {"USER"};
    
}
