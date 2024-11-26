package com.tuannv78.identity_service.dto.request;

import com.tuannv78.identity_service.enums.PermissionEnum;
import com.tuannv78.identity_service.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<PermissionEnum> permissions;
}
