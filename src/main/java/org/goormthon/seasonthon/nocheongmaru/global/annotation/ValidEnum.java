package org.goormthon.seasonthon.nocheongmaru.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.goormthon.seasonthon.nocheongmaru.global.common.EnumValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    
    String message() default "";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    Class<? extends Enum<?>> verifyClass();
    
    boolean ignoreCase() default false;
    
}
