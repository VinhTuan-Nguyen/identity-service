package com.tuannv78.identity_service.common.dto.request;

import com.tuannv78.identity_service.common.annotations.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank
    @NotNull(message = "NOT_NULL_FILED")
    @Size(min = 3, message = "INVALID_USERNAME")
    String username;

    @NotBlank
    @NotNull(message = "NOT_NULL_FILED")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "INVALID_PASSWORD"
    )
    String password;

    @NotNull(message = "NOT_NULL_FILED")
    String firstName;

    @NotNull(message = "NOT_NULL_FILED")
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

}
