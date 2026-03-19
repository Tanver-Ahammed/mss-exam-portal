package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.enrollment.ExamAttempt;
import com.mss.exam.portal.entity.enums.ExamType;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "EXAMS",
        indexes = {
                @Index(name = "IDX_EXAMS_EXAM_TYPE", columnList = "EXAM_TYPE"),
                @Index(name = "IDX_EXAMS_START_TIME", columnList = "EXAM_START_TIME")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAM_ID", updatable = false, nullable = false)
    private Long examId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE_LOCAL", nullable = false, length = 200)
    private String titleLocal;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "EXAM_TYPE", nullable = false, length = 20)
    private ExamType examType;

    @NotNull
    @Column(name = "EXAM_START_TIME", nullable = false)
    private LocalTime examStartTime;

    @NotNull
    @Column(name = "EXAM_END_TIME", nullable = false)
    private LocalTime examEndTime;

    @Min(10)
    @Max(600)
    @Column(name = "DURATION_MINUTES", nullable = false)
    private Integer durationMinutes;

    @Min(0)
    @Max(100)
    @Column(name = "PASS_MARK_PERCENT", nullable = false)
    private Integer passMarkPercent;

    @Column(name = "TOTAL_MARKS", nullable = false)
    @Builder.Default
    private Integer totalMarks = 0;

    @Min(1)
    @Column(name = "MAX_ATTEMPTS", nullable = false)
    @Builder.Default
    private Integer maxAttempts = 1;

    @Column(name = "IS_PUBLISHED", nullable = false)
    @Builder.Default
    private boolean published = false;

    @Column(name = "IS_RANDOMIZE_QUESTIONS", nullable = false)
    @Builder.Default
    private boolean randomizeQuestions = false;

    @Column(name = "IS_SHOW_RESULT_IMMEDIATELY", nullable = false)
    @Builder.Default
    private boolean showResultImmediately = true;

    @DecimalMin("0.0")
    @Column(name = "ENROLLMENT_FEE", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal enrollmentFee = BigDecimal.ZERO;

    @Column(name = "CERTIFICATE_TEMPLATE_URL")
    private String certificateTemplateUrl;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "EXAM_CONDUCTORS",
            joinColumns = @JoinColumn(
                    name = "EXAM_ID",
                    foreignKey = @ForeignKey(name = "FK_EXAM_CONDUCTORS_EXAM")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "USER_ID",
                    foreignKey = @ForeignKey(name = "FK_EXAM_CONDUCTORS_USER")
            )
    )
    @Builder.Default
    private List<User> conductedBy = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderNo ASC")
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ExamAttempt> examAttempts = new ArrayList<>();
}
