package com.tuannv78.identity_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PermissionEnum {
    CREATE_POST("CREATE_POST","Create a post"),
    REJECT_POST("REJECT_POST","Reject a post"),
    APPROVE_POST("APPROVE_POST","Approve a post");

    String name;
    String description;

    PermissionEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
