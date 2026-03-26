package com.mss.exam.portal.entity.course;

import com.mss.exam.portal.entity.exam.Question;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.*;
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
        name = "CATEGORIES",
        indexes = {
                @Index(name = "IDX_CATEGORIES_NAME", columnList = "NAME")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID", updatable = false, nullable = false)
    private Long categoryId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NAME_LOCAL", nullable = false, unique = true, length = 100)
    private String nameLocal;

    @Size(max = 500)
    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "DISPLAY_ORDER", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

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

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "COURSE_CATEGORY_MAPPINGS",
            joinColumns = @JoinColumn(
                    name = "CATEGORY_ID",
                    foreignKey = @ForeignKey(name = "FK_COURSE_CATEGORY_MAPPINGS_CATEGORY")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "COURSE_ID",
                    foreignKey = @ForeignKey(name = "FK_COURSE_CATEGORY_MAPPINGS_COURSE")
            )
    )
    @Builder.Default
    private List<Course> courses = new ArrayList<>();
}
