package edu.umsl.hester.superclickers.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SessionManager {

    private final static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences pref;

    private Context _context;

    private final int PRIVATE_MODE = 0;

    //file name
    private static final String PREF_NAME = "AndroidQuizLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    // @TODO disconnect (logout) if no interent connection

    public void setLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.apply();

        Log.d(TAG, "User login session changed");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
