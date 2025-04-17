package com.anime_social.exception;

import com.anime_social.dto.response.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<AppResponse> handlingException(Exception exception) {
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .contentType(MediaType.APPLICATION_JSON)
        // .body(AppResponse.builder()
        // .status(ErrorCode.ERROR_IS_UNCATEGORIZED.getHttpStatus())
        // .message(ErrorCode.ERROR_IS_UNCATEGORIZED.getMessage())
        // .build());
        // }

        @ExceptionHandler(CusRunTimeException.class)
        public ResponseEntity<AppResponse> handlingRuntimeException(CusRunTimeException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(AppResponse.builder()
                                                .status(exception.getErrorCode().getHttpStatus())
                                                .message(exception.getErrorCode().getMessage())
                                                .build());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<AppResponse> handlingBindException(MethodArgumentNotValidException exception) {
                String code = exception.getBindingResult().getFieldError().getDefaultMessage();
                ErrorCode errorCode = ErrorCode.valueOf(code);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(AppResponse.builder()
                                                .status(errorCode.getHttpStatus())
                                                .message(errorCode.getMessage())
                                                .build());
        }
}
