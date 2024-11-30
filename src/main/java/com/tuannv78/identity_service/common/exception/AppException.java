package com.tuannv78.identity_service.common.exception;

import com.tuannv78.identity_service.common.enums.ErrorCodeEnum;

public class AppException extends RuntimeException {

    private ErrorCodeEnum errorCode;

    public AppException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }
}
