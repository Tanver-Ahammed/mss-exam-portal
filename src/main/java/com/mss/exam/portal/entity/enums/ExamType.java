package com.mss.exam.portal.entity.enums;

/**
 * Exam delivery formats.
 * <ul>
 *   <li>{@code MCQ}       — auto-graded multiple-choice only</li>
 *   <li>{@code WRITTEN}   — student uploads PDF / images; manual grading</li>
 *   <li>{@code MIXED}     — MCQ sections + written-upload sections</li>
 *   <li>{@code PROCTORED} — live webcam-supervised sitting</li>
 *   <li>{@code TAKE_HOME} — time-windowed, no live supervision</li>
 * </ul>
 */
public enum ExamType {
    MCQ,
    WRITTEN,
    MIXED,
    PROCTORED,
    TAKE_HOME
}
