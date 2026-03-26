package com.mss.exam.portal.entity.enrollment;

import com.mss.exam.portal.entity.enums.FileType;
import com.mss.exam.portal.entity.exam.Answer;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
        name = "SUBMISSION_FILES",
        indexes = {
                @Index(name = "IDX_SUB_FILES_ANSWER_ID", columnList = "ANSWER_ID"),
                @Index(name = "IDX_SUB_FILES_ATTEMPT_ID", columnList = "ATTEMPT_ID"),
                @Index(name = "IDX_SUB_FILES_FILE_TYPE", columnList = "FILE_TYPE")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SubmissionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBMISSION_FILE_ID", updatable = false, nullable = false)
    private Long submissionFileId;

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

    @Min(1)
    @Column(name = "PAGE_NUMBER", nullable = false)
    @Builder.Default
    private Integer pageNumber = 1;

    @Column(name = "IS_REVIEWED", nullable = false)
    @Builder.Default
    private boolean reviewed = false;

    @Column(name = "THUMBNAIL_URL")
    private String thumbnailUrl;

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
            name = "ANSWER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_SUB_FILES_ANSWER")
    )
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ATTEMPT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_SUB_FILES_ATTEMPT")
    )
    private ExamAttempt attempt;
}
