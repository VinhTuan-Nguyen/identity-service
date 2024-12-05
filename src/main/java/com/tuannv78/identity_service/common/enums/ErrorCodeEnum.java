package com.tuannv78.identity_service.common.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCodeEnum {
    UNCATEGORIZED_EXCEPTION(
            9999,
            "Uncategorized error",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INVALID_KEY(
            1001,
            "Invalid message key",
            HttpStatus.BAD_REQUEST
    ),
    INVALID_USERNAME(
            1003,
            "Username must be at least {min} characters",
            HttpStatus.BAD_REQUEST
    ),
    INVALID_PASSWORD(
            1004,
            "Password at least must have 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character",
            HttpStatus.BAD_REQUEST
    ),
    INVALID_JWS(
            1008,
            "Payload of JWS object is not a valid JSON object",
            HttpStatus.BAD_REQUEST
    ),
    INVALID_DOB(
            1009,
            "Your age must be at least {min}",
            HttpStatus.BAD_REQUEST
    ),
    USER_EXISTED(
            1002,
            "User existed",
            HttpStatus.BAD_REQUEST
    ),
    USER_NOT_EXISTED(
            1005,
            "User doesn't existed",
            HttpStatus.NOT_FOUND
    ),
    ROLE_EXISTED(
            1002,
            "This role existed",
            HttpStatus.BAD_REQUEST
    ),
    ROLE_NOT_EXISTED(
            1002,
            "This role doesn't existed",
            HttpStatus.BAD_REQUEST
    ),
    PERMISSION_EXISTED(
            1002,
            "This permission existed",
            HttpStatus.BAD_REQUEST
    ),
    PERMISSION_NOT_EXISTED(
            1002,
            "This permission doesn't existed",
            HttpStatus.BAD_REQUEST
    ),
    NOT_NULL_FILED(
            1006,
            "{field} field must not be null",
            HttpStatus.UNAUTHORIZED
    ),
    UNAUTHENTICATED(
            1006,
            "Unauthenticated",
            HttpStatus.UNAUTHORIZED
    ),
    UNAUTHORIZED(
            1007,
            "You don't have permission",
            HttpStatus.FORBIDDEN
    );

    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCodeEnum(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
