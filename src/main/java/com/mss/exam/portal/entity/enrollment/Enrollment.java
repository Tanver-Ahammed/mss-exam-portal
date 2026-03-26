package com.mss.exam.portal.entity.enrollment;

import com.mss.exam.portal.entity.course.Batch;
import com.mss.exam.portal.entity.enums.EnrollmentStatus;
import com.mss.exam.portal.entity.exam.Exam;
import com.mss.exam.portal.entity.payment.Payment;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
        name = "ENROLLMENTS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UQ_ENROLLMENT_STUDENT_EXAM",
                        columnNames = {"STUDENT_ID", "EXAM_ID"}
                )
        },
        indexes = {
                @Index(name = "IDX_ENROLLMENTS_STUDENT_ID", columnList = "STUDENT_ID"),
                @Index(name = "IDX_ENROLLMENTS_EXAM_ID", columnList = "EXAM_ID"),
                @Index(name = "IDX_ENROLLMENTS_BATCH_ID", columnList = "BATCH_ID"),
                @Index(name = "IDX_ENROLLMENTS_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENROLLMENT_ID", updatable = false, nullable = false)
    private Long enrollmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 25)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING_PAYMENT;

    @Column(name = "ATTEMPTS_USED", nullable = false)
    @Builder.Default
    private Integer attemptsUsed = 0;

    @Column(name = "ENROLLED_AT")
    private LocalDateTime enrolledAt;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

    @Column(name = "IS_FEE_WAIVED", nullable = false)
    @Builder.Default
    private boolean feeWaived = false;

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
            name = "STUDENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_STUDENT")
    )
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "EXAM_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_EXAM")
    )
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "BATCH_ID",
            foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_BATCH")
    )
    private Batch batch;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ExamAttempt> attempts = new ArrayList<>();

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;
}
