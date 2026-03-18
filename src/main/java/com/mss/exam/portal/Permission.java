package com.mss.exam.portal;

import java.util.List;
import java.util.Map;

public enum Permission {

    DASHBOARD(
            "nav.home",
            Routes.DASHBOARD,
            true,
            1,
            "fa-solid fa-house",
            MenuSection.MAIN
    ),

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

    private final String messageKey;
    private final String url;
    private final boolean isInSideMenu;
    private final int viewOrder;
    private final String menuIconClass;
    private final MenuSection menuSection;

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

    public String getMessageKey() {
        return messageKey;
    }

    public String getUrl() {
        return url;
    }

    public boolean isInSideMenu() {
        return isInSideMenu;
    }

    public int getViewOrder() {
        return viewOrder;
    }

    public String getMenuIconClass() {
        return menuIconClass;
    }

    public MenuSection getMenuSection() {
        return menuSection;
    }

    public static java.util.List<Permission> sideMenuItems() {
        return java.util.Arrays.stream(values())
                .filter(Permission::isInSideMenu)
                .sorted(java.util.Comparator.comparingInt(Permission::getViewOrder))
                .toList();
    }

    public static java.util.Map<MenuSection, java.util.List<Permission>> sideMenuGrouped() {
        Map<MenuSection, List<Permission>> map = new java.util.LinkedHashMap<>();
        for (Permission p : sideMenuItems()) {
            map.computeIfAbsent(p.getMenuSection(), k -> new java.util.ArrayList<>()).add(p);
        }
        return map;
    }
}
