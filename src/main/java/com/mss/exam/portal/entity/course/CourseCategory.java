package com.mss.exam.portal.entity.course;

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
        name = "COURSE_CATEGORIES",
        indexes = {
                @Index(name = "IDX_COURSE_CATEGORIES_NAME", columnList = "NAME")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CourseCategory extends BaseEntity {

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

    @ManyToMany
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
