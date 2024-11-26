package com.tuannv78.identity_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RoleEnum {
    ADMIN("ADMIN","Admin Role"),
    USER("USER","User Role");

    String name;
    String description;

    RoleEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
