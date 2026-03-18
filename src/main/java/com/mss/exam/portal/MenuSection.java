package com.mss.exam.portal;

/**
 * Defines the logical sections of the side menu.
 * Declaration order controls the rendering order of sections in the sidebar.
 */
public enum MenuSection {

    MAIN("nav.section.main"),
    MANAGEMENT("nav.section.management"),
    ACADEMICS("nav.section.academics"),
    QUESTIONS("nav.section.questions"),
    EXAMS("nav.section.exams");

    /**
     * i18n message key for the section heading label.
     */
    private final String labelKey;

    MenuSection(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getLabelKey() {
        return labelKey;
    }
}