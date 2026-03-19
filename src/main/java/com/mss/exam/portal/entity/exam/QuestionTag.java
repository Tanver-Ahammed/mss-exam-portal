package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "QUESTION_TAGS",
        indexes = {
                @Index(name = "IDX_QUESTION_TAGS_NAME", columnList = "NAME")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class QuestionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_TAG_ID", updatable = false, nullable = false)
    private Long questionTagId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "NAME", nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Column(name = "NAME_LOCAL", nullable = false, unique = true, length = 50)
    private String nameLocal;

    @Size(max = 7)
    @Column(name = "COLOR", length = 7)
    private String color;

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
            name = "QUESTION_TAG_MAPPINGS",
            joinColumns = @JoinColumn(
                    name = "TAG_ID",
                    foreignKey = @ForeignKey(name = "FK_QUESTION_TAG_MAPPINGS_TAG")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "QUESTION_ID",
                    foreignKey = @ForeignKey(name = "FK_QUESTION_TAG_MAPPINGS_QUESTION")
            )
    )
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}
