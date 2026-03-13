package com.mss.exam.portal.entity.enums;

/**
 * Processing states for a CSV batch enrollment job.
 */
public enum BatchStatus {
    UPLOADED,
    PROCESSING,
    COMPLETED,
    PARTIALLY_FAILED,
    FAILED
}
