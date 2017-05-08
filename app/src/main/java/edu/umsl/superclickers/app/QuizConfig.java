package edu.umsl.superclickers.app;

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

    public static String URL_POST_USER_QUIZ = URL_BASE + // need to post json answer questions
            "/v1/quiz/?course_id=%1$s&user_id=%2$s&session_id=%3$s";
    public static String URL_POST_GROUP_QUIZ = URL_BASE + // need to post json answer questions
            "/v1/groupQuiz/?quiz_id=%1$s&group_id=%2$s&session_id=%3$s";
}
