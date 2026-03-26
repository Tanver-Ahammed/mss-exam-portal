package com.mss.exam.portal.entity.payment;

import com.mss.exam.portal.entity.enums.TransactionStatus;
import com.mss.exam.portal.entity.user.User;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

@Entity
@Table(
        name = "PAYMENT_TRANSACTIONS",
        indexes = {
                @Index(name = "IDX_PAY_TXN_PAYMENT_ID", columnList = "PAYMENT_ID"),
                @Index(name = "IDX_PAY_TXN_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_PAY_TXN_TRANSACTION_ID", columnList = "TRANSACTION_ID"),
                @Index(name = "IDX_PAY_TXN_GATEWAY", columnList = "GATEWAY"),
                @Index(name = "IDX_PAY_TXN_TRANSACTED_AT", columnList = "TRANSACTED_AT")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_TRANSACTION_ID", updatable = false, nullable = false)
    private Long paymentTransactionId;

    @Column(name = "TRANSACTION_ID", length = 100)
    private String transactionId;

    @NotBlank
    @Column(name = "GATEWAY", nullable = false, length = 50)
    private String gateway;

    @DecimalMin("0.00")
    @Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Column(name = "CURRENCY", nullable = false, length = 3)
    @Builder.Default
    private String currency = "BDT";

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "PAYMENT_METHOD", length = 50)
    private String paymentMethod;

    @Column(name = "PAYMENT_METHOD_DETAIL", length = 100)
    private String paymentMethodDetail;

    @Column(name = "FAILURE_REASON", length = 255)
    private String failureReason;

    @Column(name = "GATEWAY_RESPONSE", columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "TRANSACTED_AT")
    private LocalDateTime transactedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TRANSACTED_BY_USER_ID",
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_PAY_TXN_AUDIT_CREATED")
    )
    private User transactedBy;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UPDATED_BY_USER_ID",
            foreignKey = @ForeignKey(name = "FK_PAY_TXN_AUDIT_UPDATED")
    )
    private User updatedBy;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "PAYMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PAY_TXN_PAYMENT")
    )
    private Payment payment;
}
