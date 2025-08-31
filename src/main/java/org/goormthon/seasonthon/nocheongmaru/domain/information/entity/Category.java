package org.goormthon.seasonthon.nocheongmaru.domain.information.entity;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Category {
    
    HOSPITAL_FACILITIES,
    RESTAURANT_CAFE,
    ETC
    ;
    
    public static Category toCategory(String category) {
        return Stream.of(Category.values())
            .filter(type -> type.toString().equals(category.toUpperCase()))
            .findFirst()
            .get();
    }
    
}
