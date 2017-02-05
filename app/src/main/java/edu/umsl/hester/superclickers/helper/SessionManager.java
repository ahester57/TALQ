package edu.umsl.hester.superclickers.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    //file name
    private static final String PREF_NAME = "AndroidQuizLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();

        Log.d(TAG, "User login session changed");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
