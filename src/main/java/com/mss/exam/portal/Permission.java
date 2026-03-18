package com.mss.exam.portal;

import lombok.Getter;

@Getter
public enum Permission {

    // ── Core Pages ────────────────────────────────────────────────────────────
    HOME(
            "nav.home",
            Routes.HOME,
            true,
            1,
            "fa-solid fa-house",
            MenuSection.MAIN
    ),

    // ── User Management ───────────────────────────────────────────────────────
    USER(
            "nav.user",
            Routes.USER,
            true,
            2,
            "fa-solid fa-users",
            MenuSection.MANAGEMENT
    ),
    ENROLLMENT(
            "nav.enrollment",
            Routes.ENROLLMENT,
            true,
            3,
            "fa-solid fa-user-plus",
            MenuSection.MANAGEMENT
    ),

    // ── Academic Structure ────────────────────────────────────────────────────
    CATEGORY(
            "nav.category",
            Routes.CATEGORY,
            true,
            4,
            "fa-solid fa-layer-group",
            MenuSection.ACADEMICS
    ),
    COURSE(
            "nav.course",
            Routes.COURSE,
            true,
            5,
            "fa-solid fa-book-open",
            MenuSection.ACADEMICS
    ),
    BATCH(
            "nav.batch",
            Routes.BATCH,
            true,
            6,
            "fa-solid fa-people-group",
            MenuSection.ACADEMICS
    ),
    SUBJECT(
            "nav.subject",
            Routes.SUBJECT,
            true,
            7,
            "fa-solid fa-book-bookmark",
            MenuSection.ACADEMICS
    ),

    // ── Question Bank ─────────────────────────────────────────────────────────
    QUESTION_TAG(
            "nav.questionTag",
            Routes.QUESTION_TAG,
            true,
            8,
            "fa-solid fa-tags",
            MenuSection.QUESTIONS
    ),
    QUESTION(
            "nav.question",
            Routes.QUESTION,
            true,
            9,
            "fa-solid fa-circle-question",
            MenuSection.QUESTIONS
    ),

    // ── Exam Management ───────────────────────────────────────────────────────
    EXAM(
            "nav.exam",
            Routes.EXAM,
            true,
            10,
            "fa-solid fa-file-pen",
            MenuSection.EXAMS
    ),
    EXAM_ATTEMPT(
            "nav.examAttempt",
            Routes.EXAM_ATTEMPT,
            true,
            11,
            "fa-solid fa-clipboard-check",
            MenuSection.EXAMS
    );

    // ── Fields ────────────────────────────────────────────────────────────────

    /**
     * i18n message key for the menu label.
     */
    private final String messageKey;

    /**
     * The URL this permission maps to.
     */
    private final String url;

    /**
     * Whether this entry should appear in the side menu.
     */
    private final boolean isInSideMenu;

    /**
     * Rendering order in the side menu (lower = higher).
     */
    private final int viewOrder;

    /**
     * FontAwesome CSS class for the menu icon.
     */
    private final String menuIconClass;

    /**
     * The section this permission belongs to in the side menu.
     */
    private final MenuSection menuSection;

    // ── Constructor ───────────────────────────────────────────────────────────

    Permission(String messageKey,
               String url,
               boolean isInSideMenu,
               int viewOrder,
               String menuIconClass,
               MenuSection menuSection) {
        this.messageKey = messageKey;
        this.url = url;
        this.isInSideMenu = isInSideMenu;
        this.viewOrder = viewOrder;
        this.menuIconClass = menuIconClass;
        this.menuSection = menuSection;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    // ── Static helpers ────────────────────────────────────────────────────────

    /**
     * All side-menu items sorted by viewOrder.
     */
    public static java.util.List<Permission> sideMenuItems() {
        return java.util.Arrays.stream(values())
                .filter(Permission::isInSideMenu)
                .sorted(java.util.Comparator.comparingInt(Permission::getViewOrder))
                .toList();
    }

    /**
     * Side-menu items grouped by MenuSection, preserving section and item order.
     * LinkedHashMap keeps insertion order (section declaration order).
     */
    public static java.util.Map<MenuSection, java.util.List<Permission>> sideMenuGrouped() {
        return sideMenuItems().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Permission::getMenuSection,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ));
    }
}