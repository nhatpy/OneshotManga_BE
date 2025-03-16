package com.anime_social.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ERROR_IS_UNCATEGORIZED("Không xác định được lỗi", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS("Email đã được đăng ký", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("Không tìm thấy email", HttpStatus.NOT_FOUND),
    PASSWORD_IS_INCORRECT("Mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    USER_NOT_VERIFIED("Người dùng chưa được xác nhận", HttpStatus.BAD_REQUEST),
    YOU_MISSING_REQUIRED_FIELD("Cung cấp thiếu thông tin", HttpStatus.BAD_REQUEST),
    EMAIL_IS_INVALID("Email không hợp lệ", HttpStatus.BAD_REQUEST),
    PASSWORD_AT_LEAST_6_CHARACTERS("Mật khẩu phải ít nhất 6 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Chưa đăng nhập", HttpStatus.UNAUTHORIZED),
    INVALID_VERIFY_CODE("Code xác thực không hợp lệ", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS("Thể loại đã tồn tại", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("Không tìm thấy thể loại", HttpStatus.NOT_FOUND),
    MANGA_NOT_FOUND("Không tìm thấy truyện", HttpStatus.NOT_FOUND),
    CHAPTER_NOT_FOUND("Không tìm thấy chapter", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("Người dùng không tồm tại", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("Bình luận không tồn tại", HttpStatus.NOT_FOUND),
    PAYMENT_BILL_NOT_FOUND("Hóa đơn không tồn tại", HttpStatus.NOT_FOUND),
    FOLLOW_MANGA_LIST_NOT_FOUND("Danh sách theo dõi không tồn tại", HttpStatus.NOT_FOUND),
    HISTORY_EXITS("Lịch sử đã tồn tại", HttpStatus.BAD_REQUEST),
    HISTORY_NOT_FOUND("Lịch sử không tồn tại", HttpStatus.NOT_FOUND),
    MANGA_ALREADY_EXISTS("Truyện đã tồn tại", HttpStatus.BAD_REQUEST),
    CHAPTER_ALREADY_EXISTS("Chapter đã tồn tại", HttpStatus.BAD_REQUEST),
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
