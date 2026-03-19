package com.mss.exam.portal.entity.enrollment;

import com.mss.exam.portal.entity.exam.Answer;
import com.mss.exam.portal.entity.exam.Exam;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "EXAM_ATTEMPTS",
        indexes = {
                @Index(name = "IDX_EXAM_ATTEMPTS_ENROLLMENT_ID", columnList = "ENROLLMENT_ID"),
                @Index(name = "IDX_EXAM_ATTEMPTS_STARTED_AT", columnList = "STARTED_AT")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAM_ATTEMPT_ID", updatable = false, nullable = false)
    private Long examAttemptId;

    @Min(1)
    @Column(name = "ATTEMPT_NUMBER", nullable = false)
    private Integer attemptNumber;

    @Column(name = "STARTED_AT", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "SUBMITTED_AT")
    private LocalDateTime submittedAt;

    @Column(name = "SCORE")
    private Integer score;

    @Column(name = "SCORE_PERCENT")
    private Double scorePercent;

    @Column(name = "PASSED")
    private Boolean passed;

    @Column(name = "IS_SUBMITTED", nullable = false)
    @Builder.Default
    private boolean submitted = false;

    @Column(name = "IS_GRADED", nullable = false)
    @Builder.Default
    private boolean graded = false;

    @Column(name = "IP_ADDRESS", length = 45)
    private String ipAddress;

    @Column(name = "CERTIFICATE_URL")
    private String certificateUrl;

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

    @Version
    @Column(name = "VERSION", nullable = false)
    @Builder.Default
    private Long version = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ENROLLMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_EXAM_ATTEMPTS_ENROLLMENT")
    )
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "EXAM_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_EXAM_ATTEMPTS_EXAM")
    )
    private Exam exam;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}
