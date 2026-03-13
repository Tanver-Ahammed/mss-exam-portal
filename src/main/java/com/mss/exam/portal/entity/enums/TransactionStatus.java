package com.mss.exam.portal.entity.enums;

/**
 * Lifecycle states of a single {@code PaymentTransaction}.
 * Mirrors Stripe PaymentIntent / charge statuses.
 */
public enum TransactionStatus {
    PENDING,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    REFUNDED,
    CANCELLED
}
