package com.sansung_gorogoro.file_server.common.error.code;

import com.sansung_gorogoro.file_server.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum FileErrorCode implements ErrorCode {
    // ===================== Upload Session / Chunk (Domain) =====================
    UPLOAD_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "FS-001", "존재하지 않는 업로드 세션입니다."),
    UPLOAD_SESSION_EXPIRED(HttpStatus.GONE, "FS-002", "만료된 업로드 세션입니다."),
    UPLOAD_SESSION_STATUS_NOT_ACTIVE(HttpStatus.CONFLICT, "FS-003", "활성 상태가 아닌 업로드 세션입니다."),
    UPLOAD_SESSION_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "FS-004", "해당 업로드 세션에 접근 권한이 없습니다."),
    UPLOAD_SESSION_OFFSET_MISMATCH(HttpStatus.CONFLICT, "FS-005", "업로드 오프셋이 일치하지 않습니다."),
    UPLOAD_SESSION_NOT_UPLOADING(HttpStatus.BAD_REQUEST, "FS-006", "업로드 중인 세션이 아닙니다."),
    UPLOAD_CHUNK_EMPTY(HttpStatus.BAD_REQUEST, "FS-007", "업로드 청크가 비어 있습니다."),
    UPLOAD_CHUNK_SIZE_EXCEEDS_LIMIT(HttpStatus.PAYLOAD_TOO_LARGE, "FS-008", "청크 크기가 허용 범위를 초과했습니다."),

    // ===================== Request / Controller (Web) =====================
    REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "FS-010", "요청 값이 유효하지 않습니다."),
    REQUEST_BINDING_FAILED(HttpStatus.BAD_REQUEST, "FS-011", "요청 파라미터 바인딩에 실패했습니다."),
    REQUEST_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "FS-012", "요청 파라미터 타입이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "FS-013", "지원하지 않는 HTTP 메서드입니다."),

    // ===================== Internal / Infra =====================
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FS-014", "서버 내부 오류가 발생했습니다."),
    UPLOAD_TEMP_DIR_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FS-020", "파일 저장소 초기화에 실패했습니다."),

    // ===================== Validation (Input Constraints) =====================
    OWNER_USER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "FS-021", "소유자 아이디는 필수입니다."),
    OWNER_USER_ID_MUST_MORE_THAN_ZERO(HttpStatus.BAD_REQUEST, "FS-024", "소유자 아이디는 0보다 커야 합니다."),
    DECLARED_TOTAL_SIZE_REQUIRED(HttpStatus.BAD_REQUEST, "FS-022", "업로드 파일 크기는 필수입니다."),
    DECLARED_TOTAL_SIZE_MUST_MORE_THAN_ZERO(HttpStatus.BAD_REQUEST, "FS-023", "업로드 파일 크기는 0보다 커야 합니다."),
    ORIGINAL_FILE_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "FS-025", "원본 파일명은 필수입니다."),
    ORIGINAL_FILE_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "FS-026", "원본 파일명은 255자를 초과할 수 없습니다."),
    EXPIRES_AT_REQUIRED(HttpStatus.BAD_REQUEST, "FS-027", "만료시간은 필수입니다.")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}