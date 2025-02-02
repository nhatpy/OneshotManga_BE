package com.anime_social.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ERROR_IS_UNCATEGORIZED("Error is uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("Email not found", HttpStatus.NOT_FOUND),
    PASSWORD_IS_INCORRECT("Password is incorrect", HttpStatus.UNAUTHORIZED),
    USER_NOT_VERIFIED("User not verified", HttpStatus.BAD_REQUEST),
    YOU_MISSING_REQUIRED_FIELD("You missing required field", HttpStatus.BAD_REQUEST),
    EMAIL_IS_INVALID("Email is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_AT_LEAST_6_CHARACTERS("Password at least 6 characters", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Token is invalid", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Unauthorized", HttpStatus.UNAUTHORIZED),
    ;
    String message;
    HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
