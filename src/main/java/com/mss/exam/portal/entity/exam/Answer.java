package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.enrollment.ExamAttempt;
import com.mss.exam.portal.entity.enrollment.SubmissionFile;
import com.mss.exam.portal.entity.user.User;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ANSWERS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UQ_ANSWERS_ATTEMPT_QUESTION",
                        columnNames = {"ATTEMPT_ID", "QUESTION_ID"}
                )
        },
        indexes = {
                @Index(name = "IDX_ANSWERS_ATTEMPT_ID", columnList = "ATTEMPT_ID"),
                @Index(name = "IDX_ANSWERS_QUESTION_ID", columnList = "QUESTION_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_ID", updatable = false, nullable = false)
    private Long answerId;

    @Column(name = "TEXT_ANSWER", columnDefinition = "TEXT")
    private String textAnswer;

    @Column(name = "MARKS_AWARDED")
    private Integer marksAwarded;

    @Column(name = "IS_CORRECT")
    private Boolean correct;

    @Column(name = "EXAMINER_FEEDBACK", columnDefinition = "TEXT")
    private String examinerFeedback;

    @Column(name = "IS_FLAGGED", nullable = false)
    @Builder.Default
    private boolean flagged = false;

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
            name = "ATTEMPT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ANSWERS_ATTEMPT")
    )
    private ExamAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "QUESTION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ANSWERS_QUESTION")
    )
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "SELECTED_OPTION_ID",
            foreignKey = @ForeignKey(name = "FK_ANSWERS_OPTION")
    )
    private Option selectedOption;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}
