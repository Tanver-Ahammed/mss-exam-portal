package com.mss.exam.portal.entity;

import com.mss.exam.portal.entity.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate payment record for one {@link Enrollment}.
 *
 * <p>A payment is the billing header — it holds the total amount due and the
 * overall status. The individual charge attempts (Stripe calls, retries,
 * partial payments, refunds) are each stored as a child
 * {@link PaymentTransaction}.
 *
 * <pre>
 *   Enrollment  1 ──── 1  Payment  1 ──── N  PaymentTransaction
 * </pre>
 *
 * <p>Table: {@code PAYMENTS}
 */
@Entity
@Table(
    name = "PAYMENTS",
    uniqueConstraints = {
        @UniqueConstraint(name = "UQ_PAYMENTS_ENROLLMENT_ID", columnNames = "ENROLLMENT_ID")
    },
    indexes = {
        @Index(name = "IDX_PAYMENTS_ENROLLMENT_ID", columnList = "ENROLLMENT_ID"),
        @Index(name = "IDX_PAYMENTS_STATUS",        columnList = "STATUS")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    /** Total amount due for this enrollment (copied from exam fee at enroll time). */
    @DecimalMin("0.00")
    @Column(name = "AMOUNT_DUE", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountDue;

    /** Running total of all SUCCEEDED transaction amounts. */
    @Column(name = "AMOUNT_PAID", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @NotBlank
    @Column(name = "CURRENCY", nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    /**
     * Aggregate status derived from child transactions.
     * Updated by {@code PaymentService} after every transaction event.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    // ── Relationships ─────────────────────────────────────────────────────────

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "ENROLLMENT_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_PAYMENTS_ENROLLMENT")
    )
    private Enrollment enrollment;

    /**
     * All charge attempts, retries, and refunds for this payment.
     * Ordered by creation time — latest transaction reflects current state.
     */
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentTransaction> transactions = new ArrayList<>();
}
