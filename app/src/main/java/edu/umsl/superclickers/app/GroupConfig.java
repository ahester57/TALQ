package edu.umsl.superclickers.app;

/**
 * Created by Austin on 5/1/2017.
 *
 */

public class GroupConfig {
    private static String URL_BASE = "http://tblearn-api.vigilantestudio.com";

    public static String URL_GROUPS_FOR_COURSE = URL_BASE + "/v1/groups?course_id=%1$s";
    public static String URL_GROUP_BY_ID = URL_BASE + "/v1/groups/%1$s";
    public static String URL_GROUP_FOR_USER = URL_BASE +
            "v1/groupForUser/?user_id=%1$s&course_id=%2$s";
    public static String URL_GROUP_STATUS = URL_BASE +
            "v1/groupStatus/?group_id=%1$s&course_id=%2$s&quiz_id=%3$s&session_id=%4$s";
}
