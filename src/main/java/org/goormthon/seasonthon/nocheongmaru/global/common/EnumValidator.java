package org.goormthon.seasonthon.nocheongmaru.global.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.goormthon.seasonthon.nocheongmaru.global.annotation.ValidEnum;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    
    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.verifyClass();
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || enumClass == null) {
            return false;
        }
        
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return false;
        }
        
        return Arrays.stream(enumConstants)
            .anyMatch(enumConstant -> isMatchingEnum(value.trim(), enumConstant.name()));
    }
    
    private boolean isMatchingEnum(String value, String name) {
        return (ignoreCase) ? value.equalsIgnoreCase(name) : value.equals(name);
    }
    
}
