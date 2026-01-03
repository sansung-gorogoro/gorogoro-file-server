package com.sansung_gorogoro.file_server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "upload_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UploadSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "declared_total_size", nullable = false)
    private Long declaredTotalSize;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "next_offset", nullable = false)
    private Long nextOffset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadSessionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId; // from X-User-Id (gateway)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UploadSession (Long ownerUserId, Long declaredTotalSize, String originalFileName, LocalDateTime expiresAt){
        this.ownerUserId = requirePositive(ownerUserId, "ownerUserId");
        this.declaredTotalSize = requirePositive(declaredTotalSize, "declaredTotalSize");
        this.originalFileName = validateOriginalFileName(originalFileName);
        this.status = UploadSessionStatus.UPLOADING;
        this.nextOffset = 0L;
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt은 필수입니다");
    }

    public static UploadSession start(Long ownerUserId,
                                      Long declaredTotalSize,
                                      String originalFileName,
                                      LocalDateTime expiresAt) {
        return new UploadSession(ownerUserId, declaredTotalSize, originalFileName, expiresAt);
    }

    private static Long requirePositive(Long value, String fieldName) {
        if (value == null) throw new IllegalArgumentException(fieldName + "는 필수입니다.");
        if (value <= 0) throw new IllegalArgumentException(fieldName + "는 0보다 커야 합니다.");
        return value;
    }

    private static String validateOriginalFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("originalFileName은 필수입니다.");
        }
        if (originalFileName.length() > 255) {
            throw new IllegalArgumentException("originalFileName은 255자를 초과할 수 없습니다.");
        }
        return originalFileName;
    }
}
