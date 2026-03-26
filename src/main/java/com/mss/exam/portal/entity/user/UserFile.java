package com.mss.exam.portal.entity.user;

import com.mss.exam.portal.entity.enums.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "USER_FILES",
        indexes = {
                @Index(name = "IDX_USER_FILES_USER_ID", columnList = "USER_ID"),
                @Index(name = "IDX_USER_FILES_FILE_TYPE", columnList = "FILE_TYPE"),
                @Index(name = "IDX_USER_FILES_IS_ACTIVE", columnList = "IS_ACTIVE")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_FILE_ID", updatable = false, nullable = false)
    private Long userFileId;

    @NotBlank
    @Column(name = "S3_OBJECT_KEY", nullable = false)
    private String s3ObjectKey;

    @NotBlank
    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @NotBlank
    @Column(name = "ORIGINAL_FILENAME", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "CONTENT_TYPE", nullable = false, length = 100)
    private String contentType;

    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE", nullable = false, length = 30)
    private FileType fileType;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CREATED_BY_USER_ID",
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_AUDIT_CREATED_BY_USER")
    )
    private User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UPDATED_BY_USER_ID",
            foreignKey = @ForeignKey(name = "FK_AUDIT_UPDATED_BY_USER")
    )
    private User updatedBy;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "USER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_FILES_USER")
    )
    private User user;
}
