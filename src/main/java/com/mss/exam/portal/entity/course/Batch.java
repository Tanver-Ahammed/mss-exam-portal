package com.mss.exam.portal.entity.course;

import com.mss.exam.portal.entity.enums.BatchStatus;
import com.mss.exam.portal.entity.exam.Exam;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "BATCHES",
        indexes = {
                @Index(name = "IDX_BATCHES_COURSE_ID", columnList = "COURSE_ID"),
                @Index(name = "IDX_BATCHES_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BATCH_ID", updatable = false, nullable = false)
    private Long batchId;

    @NotBlank
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @NotBlank
    @Column(name = "NAME_LOCAL", nullable = false, length = 150)
    private String nameLocal;

    @Column(name = "CSV_FILENAME", nullable = false)
    private String csvFilename;

    @Column(name = "S3_OBJECT_KEY")
    private String s3ObjectKey;

    @Column(name = "ENROLLED_COUNT", nullable = false)
    @Builder.Default
    private Integer enrolledCount = 0;

    @Column(name = "FAILED_COUNT", nullable = false)
    @Builder.Default
    private Integer failedCount = 0;

    @Column(name = "SKIPPED_COUNT", nullable = false)
    @Builder.Default
    private Integer skippedCount = 0;

    @DecimalMin("0.0")
    @Max(100)
    @Column(name = "DISCOUNT", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private BatchStatus status = BatchStatus.UPLOADED;

    @Column(name = "ERROR_LOG", columnDefinition = "TEXT")
    private String errorLog;

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
            name = "COURSE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_BATCHES_COURSE")
    )
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "BATCH_INSTRUCTORS",
            joinColumns = @JoinColumn(
                    name = "BATCH_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_INSTRUCTORS_BATCH")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "USER_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_INSTRUCTORS_USER")
            )
    )
    @Builder.Default
    private List<User> instructedBy = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "BATCH_EXAMS",
            joinColumns = @JoinColumn(name = "BATCH_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_EXAMS_BATCH")),
            inverseJoinColumns = @JoinColumn(name = "EXAM_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_EXAMS_EXAM"))
    )
    @Builder.Default
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @Builder.Default
    private List<com.mss.exam.portal.entity.enrollment.Enrollment> enrollments = new ArrayList<>();
}
