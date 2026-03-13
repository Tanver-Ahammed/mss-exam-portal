package com.mss.exam.portal.entity.payment;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Records one individual charge attempt, retry, or refund event
 * under a parent {@link Payment}.
 *
 * <p>Every Stripe API call (charge, re-charge, refund, webhook event) creates
 * or updates one row here, leaving a full audit trail without mutating the
 * parent {@link Payment} header.
 *
 * <p>Table: {@code PAYMENT_TRANSACTIONS}
 */
@Entity
@Table(
        name = "PAYMENT_TRANSACTIONS",
        indexes = {
                @Index(name = "IDX_PAY_TXN_PAYMENT_ID", columnList = "PAYMENT_ID"),
                @Index(name = "IDX_PAY_TXN_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_PAY_TXN_STRIPE_CHARGE_ID", columnList = "STRIPE_CHARGE_ID"),
                @Index(name = "IDX_PAY_TXN_TRANSACTED_AT", columnList = "TRANSACTED_AT")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PaymentTransaction extends BaseEntity {

    /**
     * Stripe {@code ch_xxx} or {@code re_xxx} identifier.
     */
    @Column(name = "STRIPE_CHARGE_ID", length = 100)
    private String stripeChargeId;

    /**
     * Stripe {@code pi_xxx} PaymentIntent identifier.
     */
    @Column(name = "STRIPE_PAYMENT_INTENT_ID", length = 100)
    private String stripePaymentIntentId;

    /**
     * Stripe {@code re_xxx} Refund identifier — populated on refund transactions.
     */
    @Column(name = "STRIPE_REFUND_ID", length = 100)
    private String stripeRefundId;

    @DecimalMin("0.00")
    @Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Column(name = "CURRENCY", nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    /**
     * Payment method descriptor, e.g. {@code card}, {@code bank_transfer},
     * {@code paypal}, {@code manual}.
     */
    @Column(name = "PAYMENT_METHOD", length = 50)
    private String paymentMethod;

    /**
     * Card / wallet description for display, e.g. {@code Visa ···4242}.
     */
    @Column(name = "PAYMENT_METHOD_DETAIL", length = 100)
    private String paymentMethodDetail;

    /**
     * Timestamp when Stripe confirmed or failed the charge.
     */
    @Column(name = "TRANSACTED_AT")
    private LocalDateTime transactedAt;

    /**
     * Human-readable failure reason returned by Stripe,
     * e.g. {@code insufficient_funds}, {@code card_declined}.
     */
    @Column(name = "FAILURE_REASON", length = 255)
    private String failureReason;

    /**
     * Raw JSON body of the Stripe webhook event that triggered this row.
     * Stored for replay / audit purposes.
     */
    @Column(name = "WEBHOOK_PAYLOAD", columnDefinition = "TEXT")
    private String webhookPayload;

    // ── Relationship ──────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "PAYMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PAY_TXN_PAYMENT")
    )
    private Payment payment;
}
