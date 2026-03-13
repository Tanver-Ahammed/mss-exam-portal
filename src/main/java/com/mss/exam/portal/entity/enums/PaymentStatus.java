package com.mss.exam.portal.entity.enums;

/**
 * Aggregate status of a {@code Payment} record (across all its transactions).
 */
public enum PaymentStatus {
    UNPAID,
    PARTIALLY_PAID,
    PAID,
    REFUNDED,
    CANCELLED
}
