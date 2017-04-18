package edu.umsl.hester.superclickers.app;
/**
 * Created by Austin on 3/23/2017.
 */
public class QuizConfig {
    private static String URL_BASE = "http://tblearn-api.vigilantestudio.com";
    public static String URL_GET_QUIZ_BY_ID = "http://stin.tech/learning-api/quiz_by_id.php?id=%1$s";
    public static String URL_GET_ALL_QUIZZES = "http://stin.tech/learning-api/all_quizzes.php";
    public static String URL_QUIZZES_FOR_USER = URL_BASE + "/v1/quizzes/%1$s";
    public static String URL_QUIZ_TOKEN = URL_BASE + "/v1/quiz/%1$s/token";
    public static String // ssoID, readable courseId, Quiz UUID, token
            URL_GET_QUIZ = URL_BASE + "/v1/quiz/?user_id=%1$s&course_id=%2$s&quiz_id=%3$s&token=%4$s";
    public static String URL_GROUPS_FOR_COURSE = URL_BASE + "/v1/groups?course_id=%1$s";
    public static String URL_GROUP_BY_ID = URL_BASE + "/v1/groups/%1$s";
}