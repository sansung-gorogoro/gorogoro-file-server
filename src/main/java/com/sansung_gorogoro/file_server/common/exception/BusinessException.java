package com.sansung_gorogoro.file_server.common.exception;

import com.sansung_gorogoro.file_server.common.error.ErrorCode;
import com.sansung_gorogoro.file_server.common.response.FieldError;
import com.sansung_gorogoro.file_server.presentation.advice.FileControllerAdvice;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
    private Throwable cause;

    private BusinessException(Builder builder) {
        super(builder.getMessage(), builder.cause);
        this.httpStatus = builder.httpStatus;
        this.message = builder.getMessage();
        this.code = builder.code;
        this.cause = builder.cause;
    }
    public interface BusinessExceptionBuilder {
        BusinessExceptionBuilder withId(Long... ids);
        BusinessExceptionBuilder withField(String... fields);
        BusinessExceptionBuilder withCause(Throwable cause);

        BusinessException build();
    }

    public static BusinessExceptionBuilder builder(ErrorCode errorCode) {
        return new Builder(errorCode);
    }

    private static class Builder implements BusinessExceptionBuilder {
        private final HttpStatus httpStatus;
        private final String messageTemplate;
        private final List<Object> params = new ArrayList<>();
        private final String code;
        private Throwable cause;

        public Builder(ErrorCode errorCode) {
            this.httpStatus = errorCode.getHttpStatus();
            this.messageTemplate = errorCode.getMessage();
            this.code = errorCode.getCode();
        }

        public Builder withId(Long... ids) {
            this.params.addAll(Arrays.asList(ids));
            return this;
        }

        public Builder withField(String... fields) {
            this.params.addAll(Arrays.asList(fields));
            return this;
        }

        public Builder withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        private String getMessage() {
            if (params.isEmpty()) {
                return messageTemplate;
            }
            return String.format(messageTemplate, params.toArray());
        }

        private String getCode() {
            return code;
        }

        public BusinessException build() {
            return new BusinessException(this);
        }
    }
    @Override
    public String getMessage() {
        return message;
    }
}
