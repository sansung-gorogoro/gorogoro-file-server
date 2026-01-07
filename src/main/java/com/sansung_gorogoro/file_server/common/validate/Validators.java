package com.sansung_gorogoro.file_server.common.validate;

import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;

public class Validators {
    public static Long requirePositive(
            Long value,
            FileErrorCode requiredCode,
            FileErrorCode notPositiveCode
    ) {
        if (value == null) throw BusinessException.builder(requiredCode).build();
        if (value <= 0) throw BusinessException.builder(notPositiveCode).build();
        return value;
    }

    public static String validateString(
            String value,
            int maxLen,
            FileErrorCode requiredCode,
            FileErrorCode tooLongCode
    ) {
        if (value == null || value.isBlank()) {
            throw BusinessException.builder(requiredCode).build();
        }
        if (value.length() > maxLen) {
            throw BusinessException.builder(tooLongCode).build();
        }
        return value;
    }
}
