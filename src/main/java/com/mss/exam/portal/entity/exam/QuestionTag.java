package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@EqualsAndHashCode(callSuper = true)
public class QuestionTag extends BaseEntity {

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

    @ManyToMany
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