package com.sansung_gorogoro.file_server.presentation.advice;

import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.response.ErrorResponse;
import com.sansung_gorogoro.file_server.common.response.FieldError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;


@Slf4j
@RestControllerAdvice
public class FileControllerAdvice {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        FileErrorCode ec = FileErrorCode.REQUEST_VALIDATION_FAILED;
        List<FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse body = ErrorResponse.of(
                ec.getCode(),
                ec.getMessage(),
                errors
        );

        log.debug("Validation error(MethodArgumentNotValidException): {}", errors, ex);
        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        FileErrorCode ec = FileErrorCode.REQUEST_BINDING_FAILED;
        List<FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse body = ErrorResponse.of(
                ec.getCode(),
                ec.getMessage(),
                errors
        );

        log.debug("Binding error(BindException): {}", errors, ex);
        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        FileErrorCode ec = FileErrorCode.REQUEST_VALIDATION_FAILED;
        List<FieldError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new FieldError(
                        v.getPropertyPath().toString(),
                        v.getInvalidValue(),
                        v.getMessage()
                ))
                .toList();

        ErrorResponse body = ErrorResponse.of(
                ec.getCode(),
                ec.getMessage(),
                errors
        );

        log.debug("Validation error(ConstraintViolationException): {}", errors, ex);
        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        FileErrorCode ec = FileErrorCode.REQUEST_TYPE_MISMATCH;
        ErrorResponse body = ErrorResponse.of(
                ec.getCode(),
                ec.getMessage() + ex.getName(),
                null
        );

        log.debug("Type mismatch: {}", ex.getMessage(), ex);
        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        FileErrorCode ec = FileErrorCode.METHOD_NOT_ALLOWED;
        ErrorResponse body = ErrorResponse.of(
                ec.getCode(),
                ec.getMessage(),
                null
        );

        log.warn("Method not allowed: {}", ex.getMessage(), ex);
        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessErrorCode> handleBusinessException(BusinessException ex) {
        HttpStatus status = ex.getHttpStatus();

        BusinessErrorCode body = new BusinessErrorCode(ex.getCode(), ex.getMessage());

        // 비즈니스 예외는 warn 정도로 남겨두는 경우가 많음
        log.warn("Business exception: code={}, message={}", ex.getCode(), ex.getMessage());

        return ResponseEntity.status(status).body(body);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                null
        );

        log.error("Unexpected error", ex);
        return ResponseEntity.status(status).body(body);
    }

    public record BusinessErrorCode(
            String code,
            String message
    ) {}

    @ResponseBody
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BusinessErrorCode> handleMissingHeader(MissingRequestHeaderException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if ("X-User-Id".equals(ex.getHeaderName())) {
            FileErrorCode ec = FileErrorCode.OWNER_USER_ID_REQUIRED;
            BusinessErrorCode body = new BusinessErrorCode(ec.getCode(), ec.getMessage());
            return ResponseEntity.status(status).body(body);
        }

        FileErrorCode ec = FileErrorCode.REQUEST_BINDING_FAILED;
        BusinessErrorCode body = new BusinessErrorCode(ec.getCode(), ec.getMessage());
        return ResponseEntity.status(ec.getHttpStatus()).body(body);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BusinessErrorCode> handleNotReadable(HttpMessageNotReadableException ex) {
        FileErrorCode ec = FileErrorCode.REQUEST_BINDING_FAILED;
        return ResponseEntity.status(ec.getHttpStatus())
                .body(new BusinessErrorCode(ec.getCode(), ec.getMessage()));
    }
}
