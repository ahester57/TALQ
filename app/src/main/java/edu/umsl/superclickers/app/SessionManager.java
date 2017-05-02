package edu.umsl.superclickers.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.database.SQLiteHandlerAnswers;
import edu.umsl.superclickers.database.SQLiteHandlerCourses;
import edu.umsl.superclickers.database.SQLiteHandlerQuestions;
import edu.umsl.superclickers.database.SQLiteHandlerQuizzes;
import edu.umsl.superclickers.database.SQLiteHandlerUsers;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.User;


public class SessionManager {

    private final static String TAG = SessionManager.class.getSimpleName();

    // Shared preferences for storing user login 'token'
    private SharedPreferences pref;
    private Context _context;
    private final int PRIVATE_MODE = 0;

    //file name
    private static final String PREF_NAME = "AndroidQuizLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_QUIZ_INDEX = "quizIndex";
    private static final String KEY_QUIZ_ID = "quizId";
    private static final String KEY_GROUP_ID = "groupId";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    // @TODO disconnect (logout) if no internet connection

    public void setLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.apply();

        Log.d(TAG, "User login session changed");
    }

    public void addUserToDB(User user) {
        SQLiteHandlerUsers db = SQLiteHandlerUsers.sharedInstance(_context);
        db.addUser(user);
    }

    public User getCurrentUser() {
        SQLiteHandlerUsers db = SQLiteHandlerUsers.sharedInstance(_context);
        return db.getCurrentUser();
    }

    public String getGroupId() {
        String groupId = pref.getString(KEY_GROUP_ID, null);
        Log.d(TAG, "Group Id got = " + groupId);
        return  groupId;
    }

    public void setGroupId(String groupId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_GROUP_ID, groupId);
        editor.apply();

        Log.d(TAG, "Group Id set = " + groupId);
    }

    public void addCourseToDB(Course course) {
        SQLiteHandlerCourses db = SQLiteHandlerCourses.sharedInstance(_context);
        db.addCourse(course);
    }

    public ArrayList<Course> getEnrolledCourses() {
        SQLiteHandlerCourses db = SQLiteHandlerCourses.sharedInstance(_context);
        return db.getCurrentCourses();
    }

    public void addQuizToDB(Quiz quiz) {
        SQLiteHandlerQuestions qdb = SQLiteHandlerQuestions.sharedInstance(_context);
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(_context);
        SQLiteHandlerAnswers adb = SQLiteHandlerAnswers.sharedInstance(_context);
        db.addQuiz(quiz);
        for (Question q : quiz.getQuestions()) {
            qdb.addQuestion(q);
            for (Answer a : q.getAnswers()) {
                adb.addAnswer(a);
            }
        }
    }

    public Quiz getQuiz(String quizId) {
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(_context);
        setActiveQuiz(quizId);
        return db.getQuiz(quizId);
    }

    public ArrayList<SelectedAnswer> getSelectedAnswersFor(String questionId) {
        SQLiteHandlerAnswers db = SQLiteHandlerAnswers.sharedInstance(_context);
        return db.getSelectedAnswers(questionId);
    }

    public void setSelectedAnswersFor(ArrayList<SelectedAnswer> answers) {
        SQLiteHandlerAnswers db = SQLiteHandlerAnswers.sharedInstance(_context);
        db.removeSelectedFromQuestion(answers.get(0).getQuestionId());
        for (SelectedAnswer a : answers) {
            db.addSelectedAnswer(a);
        }
    }

    public Quiz getActiveQuiz() {
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(_context);
        return db.getQuiz(pref.getString(KEY_QUIZ_ID, null));
    }

    public void setActiveQuiz(String quizId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_QUIZ_ID, quizId);
        editor.apply();

        Log.d(TAG, "Active Quiz set = " + quizId);
    }

    public int getQuizIndex() {
        return pref.getInt(KEY_QUIZ_INDEX, 0);
    }

    public void setQuizIndex(int index) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_QUIZ_INDEX, index);
        editor.apply();

        Log.d(TAG, "Quiz index set = " + index);
    }

    public void removeQuizIndex() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_QUIZ_INDEX, -1);
        editor.apply();
        Log.d(TAG, "Quiz index removed ");
    }


    // @TODO clear database stuff all in one here
    public void clearDatabase() {
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(_context);
        SQLiteHandlerQuestions qdb = SQLiteHandlerQuestions.sharedInstance(_context);
        SQLiteHandlerAnswers adb = SQLiteHandlerAnswers.sharedInstance(_context);
        SQLiteHandlerCourses cdb = SQLiteHandlerCourses.sharedInstance(_context);
        SQLiteHandlerUsers udb = SQLiteHandlerUsers.sharedInstance(_context);
        try {
            udb.deleteAllUsers();
            db.removeAllQuizzes();
            cdb.deleteAllCourses();
            qdb.removeAllQuestions();
            adb.removeAllAnswers();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        removeQuizIndex();
    }

    public void clearActiveQuiz() {
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(_context);
        SQLiteHandlerQuestions qdb = SQLiteHandlerQuestions.sharedInstance(_context);
        SQLiteHandlerAnswers adb = SQLiteHandlerAnswers.sharedInstance(_context);

        try {
            db.removeAllQuizzes();
            qdb.removeAllQuestions();
            adb.removeAllAnswers();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        removeQuizIndex();
    }

    public boolean isQuizRunning() {
        return pref.getInt(KEY_QUIZ_INDEX, -1) != -1;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
