package com.tuannv78.identity_service.common.dto.request;

import com.tuannv78.identity_service.common.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    List<RoleEnum> roles;
}
