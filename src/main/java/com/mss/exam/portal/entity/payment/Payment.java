package com.mss.exam.portal.entity.payment;

import com.mss.exam.portal.entity.enums.PaymentStatus;
import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
        name = "PAYMENTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_PAYMENTS_ENROLLMENT_ID", columnNames = "ENROLLMENT_ID")
        },
        indexes = {
                @Index(name = "IDX_PAYMENTS_ENROLLMENT_ID", columnList = "ENROLLMENT_ID"),
                @Index(name = "IDX_PAYMENTS_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID", updatable = false, nullable = false)
    private Long paymentId;

    @DecimalMin("0.00")
    @Column(name = "AMOUNT_DUE", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountDue;

    @Column(name = "AMOUNT_PAID", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @NotBlank
    @Column(name = "CURRENCY", nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ENROLLMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PAYMENTS_ENROLLMENT")
    )
    private Enrollment enrollment;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PaymentTransaction> transactions = new ArrayList<>();
}
