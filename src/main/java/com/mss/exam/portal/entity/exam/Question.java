package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.course.Category;
import com.mss.exam.portal.entity.enums.QuestionType;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.*;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "QUESTIONS",
        indexes = {
                @Index(name = "IDX_QUESTIONS_EXAM_ID", columnList = "EXAM_ID"),
                @Index(name = "IDX_QUESTIONS_ORDER_NO", columnList = "ORDER_NO")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID", updatable = false, nullable = false)
    private Long questionId;

    @NotBlank
    @Column(name = "QUESTION_TEXT", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "QUESTION_TYPE", nullable = false, length = 20)
    private QuestionType questionType;

    @Min(1)
    @Column(name = "MARKS", nullable = false)
    @Builder.Default
    private Integer marks = 1;

    @Column(name = "ORDER_NO", nullable = false)
    @Builder.Default
    private Integer orderNo = 0;

    @Column(name = "EXPLANATION", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "ANSWER_KEYWORDS", columnDefinition = "TEXT")
    private String answerKeywords;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "IS_MANDATORY", nullable = false)
    @Builder.Default
    private boolean mandatory = true;

    @Column(name = "QUESTION_YEAR")
    private Integer questionYear;

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
            name = "CATEGORY_ID",
            foreignKey = @ForeignKey(name = "FK_QUESTIONS_CATEGORY")
    )
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "SUBJECT_ID",
            foreignKey = @ForeignKey(name = "FK_QUESTIONS_SUBJECT")
    )
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "EXAM_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTIONS_EXAM")
    )
    private Exam exam;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "QUESTION_TAG_MAPPINGS",
            joinColumns = @JoinColumn(
                    name = "QUESTION_ID",
                    foreignKey = @ForeignKey(name = "FK_QUESTION_TAG_MAPPINGS_QUESTION")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "TAG_ID",
                    foreignKey = @ForeignKey(name = "FK_QUESTION_TAG_MAPPINGS_TAG")
            )
    )
    @Builder.Default
    private List<QuestionTag> tags = new ArrayList<>();
}
