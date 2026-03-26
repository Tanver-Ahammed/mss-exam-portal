package com.mss.exam.portal;

public final class Routes {

    public static final String DASHBOARD = "/dashboard";
    public static final String USER = "/user";
    public static final String API_USER = "/api/user";
    public static final String CATEGORY = "/category";
    public static final String COURSE = "/course";
    public static final String BATCH = "/batch";
    public static final String SUBJECT = "/subject";
    public static final String QUESTION_TAG = "/question-tag";
    public static final String EXAM = "/exam";
    public static final String QUESTION = "/question";
    public static final String ENROLLMENT = "/enrollment";
    public static final String EXAM_ATTEMPT = "/exam-attempt";
    public static final String API_GEO = "/api/geo";
    public static final String API_DIVISIONS = API_GEO + "/divisions";
    public static final String API_DISTRICTS = API_GEO + "/division/{divisionId}/districts";
    public static final String API_UPAZILAS = API_GEO + "/district/{districtId}/upazilas";

}