package edu.umsl.superclickers.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


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

    public void setQuizIndex(int index) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_QUIZ_INDEX, index);
        editor.apply();

        Log.d(TAG, "Quiz index set = " + index);
    }

    public int getQuizIndex() {
       return pref.getInt(KEY_QUIZ_INDEX, 0);
    }

    public boolean isQuizRunning() {
        return pref.contains(KEY_QUIZ_INDEX);
    }

    public void removeQuizIndex() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(KEY_QUIZ_INDEX);
        editor.apply();
        Log.d(TAG, "Quiz index removed ");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
