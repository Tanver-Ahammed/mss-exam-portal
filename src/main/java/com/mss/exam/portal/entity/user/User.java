package com.mss.exam.portal.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mss.exam.portal.entity.course.Batch;
import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.enums.Role;
import com.mss.exam.portal.entity.enums.UserStatus;
import com.mss.exam.portal.entity.exam.Exam;
import com.mss.exam.portal.entity.geo.District;
import com.mss.exam.portal.entity.geo.Division;
import com.mss.exam.portal.entity.geo.Upazila;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_USERS_EMAIL", columnNames = "EMAIL"),
                @UniqueConstraint(name = "UQ_USERS_PHONE", columnNames = "PHONE"),
                @UniqueConstraint(name = "UQ_USERS_USERNAME", columnNames = "USERNAME")
        },
        indexes = {
                @Index(name = "IDX_USERS_EMAIL", columnList = "EMAIL"),
                @Index(name = "IDX_USERS_PHONE", columnList = "PHONE"),
                @Index(name = "IDX_USERS_ROLE", columnList = "ROLE"),
                @Index(name = "IDX_USERS_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", updatable = false, nullable = false)
    private Long userId;

    @NotBlank
    @Column(name = "NAME", length = 80)
    private String name;

    @NotBlank
    @Column(name = "NAME_LOCAL", length = 80)
    private String nameLocal;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @NotBlank
    @Column(name = "PHONE", length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DIVISION_ID",
            foreignKey = @ForeignKey(name = "FK_USERS_DIVISION")
    )
    private Division division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DISTRICT_ID",
            foreignKey = @ForeignKey(name = "FK_USERS_DISTRICT")
    )
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UPAZILA_ID",
            foreignKey = @ForeignKey(name = "FK_USERS_UPAZILA")
    )
    private Upazila upazila;

    @Email
    @Column(name = "EMAIL", nullable = false, length = 120)
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 30)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserFile> userFiles = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @ManyToMany(mappedBy = "instructedBy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Batch> instructedCourses = new ArrayList<>();

    @ManyToMany(mappedBy = "conductedBy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Exam> conductedExams = new ArrayList<>();
}
