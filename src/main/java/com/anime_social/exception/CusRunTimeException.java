package com.anime_social.exception;

public class CusRunTimeException extends RuntimeException {
    ErrorCode errorCode;
    public CusRunTimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
